package com.lloydsbyte.jetpackdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lloydsbyte.jetpackdemo.ui.theme.JetpackDemoTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            JetpackDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

//                    ImageCard(
//                        painter = painterResource(id = R.drawable.ic_bot),
//                        contentDescription = "I am the description",
//                        title = "Hello I am a test, can I say completed?"
//                    )
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                    columnList()
//                    lazyColumnDemo()

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DemoPreview() {
//    MyDayTheme {
//    Greeting("Jeremy")
//    ImageCard(
//        painter = painterResource(id = R.drawable.demo_image),
//        contentDescription = "I am the description",
//        title = "Hello I am a test, can I say completed?"
//    )
//    textDemo()
//    }
//    columnList()
    ConstrainLayoutDemo()
}

@Composable
fun sideEffects() {
    var text by remember {
        mutableStateOf("")
    }
    //If key is true, it will run only once
    //key could also be a value that would cancel the previous ie animation and start a new animation
    LaunchedEffect(key1 = text) { //Provides a CoroutineScope
        //If text is changed, it will run the composable again
        println(true)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE){
                println("On pause called")
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            //Dispose of observer when composable lifecycle has ended
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SideEffect {
        //If you have to update something else with your code after the compose gets updated, this is useful
        println("Called after every successful recomposition")
    }
}

@Composable
fun SnapShotFlowDemo() {
    val scaffoldState = rememberScrollState()
    LaunchedEffect(key1 = scaffoldState) {
        snapshotFlow { scaffoldState }
            .mapNotNull { it.value }
            .distinctUntilChanged()
            .collect { message ->
                println("Something has been done... $message")
            }
    }
}


@Composable
fun ConstrainLayoutDemo() {
    //Dependency needs to be added
    val constraints = ConstraintSet {
        val greenbox = createRefFor("greenbox")
        val redbox = createRefFor("redbox")
        val guideline = createGuidelineFromTop(0.5f)

        constrain(greenbox) {
            top.linkTo(guideline)
            start.linkTo(parent.start)
            width = Dimension.value(100.dp)
            height = Dimension.value(100.dp)
        }
        constrain(redbox) {
            top.linkTo(parent.top)
            start.linkTo(greenbox.end)
            end.linkTo(parent.end)
            width = Dimension.value(100.dp)
            height = Dimension.value(100.dp)
        }
        createHorizontalChain(greenbox, redbox)
//        createHorizontalChain(greenbox, redbox, chainStyle = ChainStyle.Packed)
    }

    ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .background(Color.Green)
            .layoutId("greenbox"))
        Box(modifier = Modifier
            .background(Color.Red)
            .layoutId("redbox"))
    }
}


@Composable
fun lazyColumnDemo() {
    LazyColumn {
        itemsIndexed(
            listOf("This", "Is", "Jetpack", "Compose")
        ) { index, string ->
            Text(
                text = string,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
            )
        }
//        items(5000) {
//            Text(
//                text = "Item $it",
//                color = Color.Black,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Normal,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(vertical = 24.dp)
//            )
//        }
    }
}

@Composable
fun columnList() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        for (i in 1..50) {
            Text(
                text = "Item $i",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
            )
        }
    }
}

@Composable
fun snackBarDemo(modifier: Modifier) {
    //Allows us to use material design as normal, snackbar example
    var textFieldState by remember {
        mutableStateOf("")
    }
    val snackbarHostState = remember { SnackbarHostState() }
    var scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            TextField(
                value = textFieldState,
                label = {
                    Text(text = "Enter you name")
                },
                onValueChange = {
                    textFieldState = it
                },
                singleLine = true,
                modifier = modifier.fillMaxWidth()
            )
            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Hello $textFieldState")
                }
            }) {
                Text(text = "Press me")
            }
        }
    }
}


@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White
                            ),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(text = title, style = TextStyle(color = Color.Black, fontSize = 16.sp))
            }
        }
    }
}

@Composable
fun textDemo() {
    val fontFamily = FontFamily(
        Font(R.font.danfo_regular, FontWeight.Normal)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Green,
                        fontSize = 50.sp
                    )
                ) {
                    append("J")
                }
                append("etpack ")
                withStyle(
                    style = SpanStyle(
                        color = Color.Green,
                        fontSize = 50.sp
                    )
                ) {
                    append("C")
                }
                append("ompose")
            },
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color.Gray)
            .border(width = 5.dp, Color.Magenta)
            .padding(20.dp)
            .border(width = 5.dp, Color.Blue)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier
                .clickable {
                    Toast.makeText(context, "IT WORKED", Toast.LENGTH_LONG).show()
                }
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Goodbye $name",
            modifier = modifier
        )
    }
}
