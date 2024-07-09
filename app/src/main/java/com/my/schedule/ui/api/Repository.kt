package com.my.schedule.ui.api

import com.my.schedule.ui.api.internal.network.ApiTask
import com.my.schedule.ui.api.internal.data.model.toExternalModel
import com.my.schedule.ui.api.internal.data.remote.ServiceApi
import com.my.schedule.ui.api.model.Post

class Repository {
    private val serviceApi: ServiceApi = ServiceApi(3000)

    suspend fun post(): Post.State {
        return when (val task = serviceApi.post()) {
            is ApiTask.Success -> {
                Post.Success(task.body.map {
                    it.toExternalModel()
                })
            }

            is ApiTask.Fail -> {
                Post.ConnectFailed(task.exception.toString())
            }

            is ApiTask.ErrorBody -> {
                Post.ServerError(task.code.toString(), task.errorBody)
            }
        }
    }
}