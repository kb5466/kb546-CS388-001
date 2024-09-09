package com.example.composebasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composebasics.ui.theme.ComposeBasicsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeBasicsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun ComposeQuadrant(t1: String, m1: String,t2: String, m2: String,t3: String, m3: String,t4: String, m4: String, modifier: Modifier=Modifier) {
    Column(Modifier.fillMaxWidth()){
        Row(Modifier.weight(1f)){
            ComposableCard(t1,m1,Color(0xFFEADDFF),Modifier.weight(1f))
            ComposableCard(t2,m2,Color(0xFFD0BCFF),Modifier.weight(1f))
        }
//        Row(Modifier.weight(1f)){
//            ComposableCard(t3,m3,Color(0xFFB69DF8),Modifier.weight(1f))
//            ComposableCard(t4,m4,Color(0xFFF6EDFF),Modifier.weight(1f))
//        }
    }

}
@Composable
private fun ComposableCard(
    title:String,
    description: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
//            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
        ,verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun ComposeArticle(title: String, intro: String, description: String, modifier: Modifier = Modifier){
    val image = painterResource(R.drawable.bg_compose_background);
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            painter = image,
            contentDescription = null

        )
        Text(
            text = title,
            fontSize = 24.sp,
            textAlign = TextAlign.Left,
            modifier = modifier
                .padding(16.dp)

        )
        Text(
            text = intro,
            textAlign = TextAlign.Justify,
            modifier = modifier
                .padding(16.dp)
        )
        Text(
            text = description,
            textAlign = TextAlign.Justify,
            modifier = modifier
                .padding(16.dp)
        )
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeBasicsTheme {
//        ComposeArticle(
//            stringResource(R.string.title),
//            stringResource(R.string.intro),
//            stringResource(R.string.description)
//        )
        ComposeQuadrant(
            "Text composable",
            "Displays text and follows the recommended Material Design guidelines.",
            "Image composable",
            "Creates a composable that lays out and draws a given Painter class object.",
            "Row composable",
            "A layout composable that places its children in a horizontal sequence.",
            "Column composable",
            "A layout composable that places its children in a vertical sequence."
        )
    }
}