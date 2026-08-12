package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskdetail.AddTaskScreen
import com.example.taskdetail.TaskDetailScreen
import com.example.taskdetail.TaskDetailViewModel
import com.example.tasklist.TaskListScreen
import com.example.tasklist.TaskListViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "list") {
        composable("list") {
            val viewModel: TaskListViewModel = hiltViewModel()
            TaskListScreen(
                viewModel = viewModel,
                onTaskClick = { id -> navController.navigate("detail/$id") },
                onAddClick = { navController.navigate("add") }
            )
        }
        composable("add") {
            AddTaskScreen(onTaskCreated = { navController.popBackStack() })
        }
        composable(
            route = "detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) {
            val viewModel: TaskDetailViewModel = hiltViewModel()
            TaskDetailScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
    }
}