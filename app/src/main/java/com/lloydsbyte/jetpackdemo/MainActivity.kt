package com.lloydsbyte.jetpackdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lloydsbyte.jetpackdemo.ui.theme.JetpackDemoTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.Vector


data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            JetpackDemoTheme {

                var selectedIconIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                val items = listOf(
                    BottomNavItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unSelectedIcon = Icons.Outlined.Home,
                        hasNews = false
                    ),
                    BottomNavItem(
                        title = "Chat",
                        selectedIcon = Icons.Filled.Email,
                        unSelectedIcon = Icons.Outlined.Email,
                        hasNews = false,
                        badgeCount = 22
                    ),
                    BottomNavItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unSelectedIcon = Icons.Outlined.Settings,
                        hasNews = true
                    )
                )

                val rootNavController = rememberNavController()
                val navBackStachEntry by rootNavController.currentBackStackEntryAsState()

                Surface {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        //Bottom Nav Bar
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, bottomNavItem ->
                                    NavigationBarItem(
                                        selected = selectedIconIndex == index,
                                        onClick = {
                                            selectedIconIndex = index
                                            //navController.navigation(bottomNavItem.title , if title is same as nav value
                                            rootNavController.navigate(bottomNavItem.title) {
                                                //Pops the backstack list, saves in memory
                                                popUpTo(rootNavController.graph.findStartDestination().id) {
                                                    saveState =
                                                        true //Save the state of the previous back stack
                                                }
                                                launchSingleTop =
                                                    true //Does not push new screen on the backstack, uses existing
                                                restoreState =
                                                    true //Restores the state of the last stack
                                            }
                                        },
                                        label = {
                                            Text(text = bottomNavItem.badgeCount.toString())
                                        },
                                        icon = {
                                            BadgedBox(badge = {
                                                if (bottomNavItem.badgeCount != null) {
                                                    Badge {
                                                        Text(text = bottomNavItem.badgeCount.toString())
                                                    }
                                                } else if (bottomNavItem.hasNews) {
                                                    Badge()
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = if (index == selectedIconIndex) bottomNavItem.selectedIcon else bottomNavItem.unSelectedIcon,
                                                    contentDescription = bottomNavItem.title
                                                )
                                            }
                                        })
                                }
                            }
                        }
                    ) { _ ->


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

//                        AnimationDemo()

                        /** Instead of a normal navhost that is created in the navhost controller, build it like so **/
                        NavHost(navController = rootNavController, startDestination = "home") {
                            composable("home") {
                                HomeNavHost()
                            }
                            composable("Middle") {
                                HomeNavHost()//2
                            }
                            composable("LastScreen") {
                                HomeNavHost()//3
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeNavHost() {
    val homeNavController = rememberNavController()
    NavHost(homeNavController, startDestination = "home1") {
        for (i in 1..10) {
            composable("home$i") {
                GenericScreen(
                    text = "Home $i",
                    onNextClick = {
                        if (i < 10) {
                            homeNavController.navigate("home${i + 1}")
                        }
                    }
                )
            }
        }
    }
}

    @Composable
    fun GenericScreen(
        text: String,
        onNextClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = text)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onNextClick) {
                Text("Next")
            }
        }
    }


    /**
     * Use one host per tab to have their own backstack, so one tab is unaware of the other tabs screens
     */


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
//    ConstrainLayoutDemo()
    AnimationDemo()
}

@Composable
fun AnimationDemo() {
    var isVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize()) {

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                isVisible = !isVisible
            }) {
            Column(modifier = Modifier) {
                Text(
                    text = "Hello",
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
//                AnimatedVisibility(isVisible) {
//                    Text(
//                        text = "Say goodbye", modifier = Modifier
//                            .padding(14.dp)
//                            .width(100.dp)
//                            .height(80.dp)
//                    )
//                }
            }
        }

        Text(
            modifier = Modifier
                .background(Color.LightGray)
                .animateContentSize()
                .width(240.dp)
                .align(Alignment.CenterHorizontally)
                .padding(30.dp),
            maxLines = if (isVisible) 6 else 2,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
        )

        Text(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.LightGray)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .width(240.dp)
                .align(Alignment.CenterHorizontally)
                .padding(30.dp),
            maxLines = if (isVisible) 10 else 2,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
        )
    }
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
            if (event == Lifecycle.Event.ON_PAUSE) {
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
        Box(
            modifier = Modifier
                .background(Color.Green)
                .layoutId("greenbox")
        )
        Box(
            modifier = Modifier
                .background(Color.Red)
                .layoutId("redbox")
        )
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
    ) { _ ->
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
