package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.domain.usecase.TaskUseCases
import com.example.taskdetail.AddTaskScreen
import com.example.taskdetail.TaskDetailScreen
import com.example.taskdetail.TaskDetailViewModel
import com.example.taskdetail.TaskDetailViewModelFactory
import com.example.tasklist.TaskListScreen
import com.example.tasklist.TaskListViewModel
import com.example.tasklist.TaskListViewModelFactory

@Composable
fun AppNavHost(navController: NavHostController, useCases: TaskUseCases) {
    NavHost(navController, startDestination = "list") {
        composable("list") {
            val viewModel: TaskListViewModel = viewModel(factory = TaskListViewModelFactory(useCases))
            TaskListScreen(
                viewModel = viewModel,
                onTaskClick = { id -> navController.navigate("detail/$id") },
                onAddClick = { navController.navigate("add") }
            )
        }
        composable("add") {
            AddTaskScreen(
                onTaskCreated = { navController.popBackStack() },
                createTask = { short, full -> useCases.createTask(short, full) }
            )
        }
        composable(
            route = "detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId").orEmpty()
            val viewModel: TaskDetailViewModel = viewModel(
                factory = TaskDetailViewModelFactory(useCases, taskId)
            )
            TaskDetailScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
    }
}