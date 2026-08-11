package com.example.domain.usecase

class TaskUseCases(
    val observeTasks: ObserveTasksUseCase,
    val observeTask: ObserveTaskByIdUseCase,
    val createTask: CreateTaskUseCase,
    val takeInProgress: TakeInProgressUseCase,
    val complete: CompleteTaskUseCase,
    val delete: DeleteTaskUseCase
)