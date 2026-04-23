package com.xyzone.blogs.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyzone.blogs.app.android.ui.theme.XYZoneTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XYZoneTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { 
                                Text(
                                    "XYZone Blogs", 
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold // 标题加粗，更有视觉冲击力
                                ) 
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    containerColor = Color.Transparent // 设置为透明，好让下面的渐变背景露出来
                ) { padding ->
                    // 核心“柔化”技巧：增加渐变背景层
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), // 顶部淡淡的暖橙色
                                        MaterialTheme.colorScheme.background // 底部回归正常背景色
                                    )
                                )
                            )
                            .padding(padding)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                BlogCard(
                                    title = "✨ UI 柔和进化完成",
                                    snippet = "我们实装了大圆角、微透明阴影以及动态渐变背景。现在的 XYZone 看起来充满了现代感，不再是死板的方块了喵！"
                                )
                            }
                            item {
                                BlogCard(
                                    title = "🚀 下一站：香港机房数据对接",
                                    snippet = "正在准备接入 Retrofit。我们将通过域名解析到 blog.xyzone.asia，把你的 Halo 2.x 文章实时同步到这里。"
                                )
                            }
                            item {
                                BlogCard(
                                    title = "🎨 关于白橙色调的执着",
                                    snippet = "暖橙色不仅是视觉符号，更是极客圈的一种温度。无论是 Android 15 还是未来的新系统，这抹颜色都会一直陪伴..."
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlogCard(title: String, snippet: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // 极大圆角：柔化的核心
        colors = CardDefaults.cardColors(
            // 微透明处理，让背景的渐变能透出一丁点，增加呼吸感
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // 细腻阴影
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) // 橙色微光描边
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = snippet,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 22.sp // 增加行高，看着不累
            )
            
            // 装饰性的小标签，精致度提升 50%
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    text = "View Article →",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
