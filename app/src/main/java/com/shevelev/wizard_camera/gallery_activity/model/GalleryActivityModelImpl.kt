package com.shevelev.wizard_camera.gallery_activity.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBaseImpl
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.mapping.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GalleryActivityModelImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val db: DbCore,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner
) : ModelBaseImpl(),
    GalleryActivityModel {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private var offset = 0

    private val photosList = mutableListOf<PhotoShot>()

    private var updateInProgress = false

    private val _photos = MutableLiveData<List<PhotoShot>>(photosList)
    override val photos: LiveData<List<PhotoShot>> = _photos

    override val pageSize: Int = PAGE_SIZE

    override suspend fun loadPage() {
        if(updateInProgress) {
            return
        }

        try {
            updateInProgress = true

            val dbData = withContext(dispatchersProvider.ioDispatcher) {
                db.photoShot.readPaged(PAGE_SIZE, offset).map { it.map() }
            }

            photosList.addAll(dbData)
            offset += PAGE_SIZE
            _photos.value = photosList
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        } finally {
            updateInProgress = false
        }
    }

    override suspend fun delete(position: Int) {
        if(updateInProgress) {
            return
        }

        try {
            updateInProgress = true

            val shotItem = photosList[position]

            val deletedFile = withContext(dispatchersProvider.ioDispatcher) {
                db.photoShot.deleteById(shotItem.id)
                filesHelper.removeShotFileByName(shotItem.fileName)
            }

            mediaScanner.processDeletedShot(deletedFile)

            photosList.removeAt(position)
            _photos.value = photosList
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        } finally {
            updateInProgress = false
        }
    }

    override fun getShot(position: Int): PhotoShot = photosList[position]
}