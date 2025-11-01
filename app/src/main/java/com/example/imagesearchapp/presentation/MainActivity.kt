package com.example.imagesearchapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.imagesearchapp.presentation.theme.ImageSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            var query by rememberSaveable { mutableStateOf("") }

            ImageSearchAppTheme {
                Scaffold(
                    topBar = {
                        TextField(
                            value = query,
                            onValueChange = {
                                query = it
                                viewModel.updateQuery(query)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            label = { Text("Search images...") },
                            singleLine = true
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val images = viewModel.images.collectAsLazyPagingItems()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        when {
            images.loadState.refresh is LoadState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            images.loadState.refresh is LoadState.Error -> {
                val error = (images.loadState.refresh as LoadState.Error).error
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${error.localizedMessage ?: "Unknown error"}")
                    Button(onClick = { images.retry() }) {
                        Text("Retry")
                    }
                }
            }

            images.itemCount == 0 -> {
                Text(
                    text = "Nothing Found",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(
                        images.itemCount,
                        key = { index -> images[index]?.uuid ?: index }) { index ->
                        val image = images[index]
                        if (image != null) {
                            AsyncImage(
                                model = image.imageUri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                    }

                    // Append loading indicator
                    if (images.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    // Append error handler
                    if (images.loadState.append is LoadState.Error) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(onClick = { images.retry() }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/*
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            var query by rememberSaveable { mutableStateOf("") }
            ImageSearchAppTheme {
                Scaffold(
                    topBar = {
                        TextField(
                            value = query, onValueChange = {
                                query = it
                                viewModel.updateQuery(query)
                            }, modifier = Modifier
                                .safeContentPadding()
                                .fillMaxSize()
                        )
                    }, modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val images = viewModel.images.collectAsLazyPagingItems()

    if (images.loadState.refresh is LoadState.NotLoading) {
        if (images.itemCount == 0) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nothing Found")
            }
        }
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        if (images.loadState.prepend is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        if (images.loadState.prepend is LoadState.Error) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { images.retry() }) {
                        Text(text = "Retry")
                    }
                }
            }
        }

        if (images.loadState.refresh is LoadState.NotLoading) {
            if (images.itemCount != 0) {
                items(
                    count = images.itemCount,
                    key = images.itemKey { it.uuid },
                    contentType = images.itemContentType { "contenttype" },
                ) { index ->
                    AsyncImage(
                        model = images[index], contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }

        if (images.loadState.append is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        if (images.loadState.append is LoadState.Error) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { images.retry() }) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}

 */
