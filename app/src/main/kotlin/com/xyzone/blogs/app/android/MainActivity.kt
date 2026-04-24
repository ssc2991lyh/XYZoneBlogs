package com.xyzone.blogs.app.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

// --- 数据模型 ---
@Serializable data class HaloResponse(val items: List<Post>)
@Serializable data class Post(val spec: PostSpec)
@Serializable data class PostSpec(val title: String, val excerpt: String? = null)

interface HaloApi {
    @GET("apis/content.halo.run/v1alpha1/posts")
    suspend fun getPosts(@Header("Authorization") auth: String): HaloResponse
}

class MainActivity : ComponentActivity() {
    
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://blog.xyzone.asia/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val apiService = retrofit.create(HaloApi::class.java)

    @OptIn(ExperimentalMaterial3Api::class) 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // 这里之前少了个右括号喵

        // 获取本地存储的工具
        val sharedPref = getSharedPreferences("config", Context.MODE_PRIVATE)

        setContent {
            var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
            var isLoading by remember { mutableStateOf(false) }
            // 从存储中读取 Token
            var savedToken by remember { mutableStateOf(sharedPref.getString("token", "") ?: "") }
            var showDialog by remember { mutableStateOf(savedToken.isEmpty()) }
            var inputToken by remember { mutableStateOf("") }
            
            val scope = rememberCoroutineScope()

            // 封装一个刷新的方法
            fun fetchData(token: String) {
                if (token.isBlank()) return
                isLoading = true
                scope.launch {
                    try {
                        val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                        val response = apiService.getPosts(formattedToken)
                        posts = response.items
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        isLoading = false
                    }
                }
            }

            // 初始加载
            LaunchedEffect(savedToken) {
                if (savedToken.isNotEmpty()) fetchData(savedToken)
            }

            MaterialTheme {
                Scaffold(
                    topBar = { 
                        TopAppBar(
                            title = { Text("XYZone Blog") },
                            actions = {
                                TextButton(onClick = { showDialog = true }) {
                                    Text("设置 Token")
                                }
                            }
                        ) 
                    }
                ) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        } else if (posts.isEmpty() && !showDialog) {
                            Text("暂无数据，请检查网络或 Token", modifier = Modifier.align(Alignment.Center))
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(posts) { post ->
                                    BlogCard(post.spec.title, post.spec.excerpt ?: "点击阅读全文...")
                                }
                            }
                        }

                        // --- Token 输入弹窗 ---
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { if(savedToken.isNotEmpty()) showDialog = false },
                                title = { Text("配置个人令牌") },
                                text = {
                                    Column {
                                        Text("请输入你的 Halo PAT (pat_...)")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextField(
                                            value = inputToken,
                                            onValueChange = { inputToken = it },
                                            placeholder = { Text("pat_xxxxxxxx") }
                                        )
                                    }
                                },
                                confirmButton = {
                                    Button(onClick = {
                                        sharedPref.edit().putString("token", inputToken).apply()
                                        savedToken = inputToken
                                        showDialog = false
                                        fetchData(inputToken)
                                    }) { Text("保存并刷新") }
                                }
                            )
                        }
                    }
                }
            }
        }
    } // onCreate 的正确结尾
}

@Composable
fun BlogCard(title: String, snippet: String) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = snippet, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
        }
    }
}
