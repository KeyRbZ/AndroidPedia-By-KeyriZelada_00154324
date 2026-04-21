package com.pdm0126.taller1_00154324

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val funFact: String
)

val quizQuestions = listOf(
    Question(
        id = 1,
        question = "¿Cuál fue el propósito original del sistema operativo Android antes de adaptarse a teléfonos móviles?",
        options = listOf(
            "Videojuegos",
            "Cámaras inteligentes",
            "Computadoras portátiles",
            "Televisores inteligentes"
        ),
        correctAnswer = "Cámaras inteligentes",
        funFact = "Android originalmente fue diseñado para cámaras inteligentes antes de enfocarse en teléfonos móviles."
    ),
    Question(
        id = 2,
        question = "¿Qué característica distingue a los nombres de las versiones del sistema operativo Android?",
        options = listOf(
            "Están basados en nombres de científicos",
            "Se nombran con ciudades del mundo",
            "Utilizan nombres de postres en orden alfabético",
            "Son números aleatorios sin significado"
        ),
        correctAnswer = "Utilizan nombres de postres en orden alfabético",
        funFact = "Las versiones de Android tradicionalmente reciben nombres de postres siguiendo el orden alfabético, como Cupcake, Donut y KitKat."
    ),
    Question(
        id = 3,
        question = "¿En qué proyecto de la NASA se utilizaron por primera vez smartphones con Android dentro de la Estación Espacial Internacional?",
        options = listOf(
            "Apollo",
            "Voyager",
            "SPHERES",
            "Hubble"
        ),
        correctAnswer = "SPHERES",
        funFact = "La NASA utilizó smartphones Android en el proyecto SPHERES dentro de la ISS para controlar robots."
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPediaApp()
        }
    }
}

@Composable
fun AndroidPediaApp() {
    var screen by rememberSaveable { mutableStateOf("welcome") }
    var currentQuestion by rememberSaveable { mutableStateOf(0) }
    var score by rememberSaveable { mutableStateOf(0) }
    var selectedAnswer by rememberSaveable { mutableStateOf(-1) }
    var answered by rememberSaveable { mutableStateOf(false) }

    when (screen) {
        "welcome" -> WelcomeScreen {
            screen = "quiz"
        }

        "quiz" -> {
            val question = quizQuestions[currentQuestion]

            QuizScreen(
                question = question,
                questionNumber = currentQuestion + 1,
                totalQuestions = quizQuestions.size,
                score = score,
                selectedAnswer = selectedAnswer,
                answered = answered,
                onAnswerSelected = { index ->
                    if (!answered) {
                        selectedAnswer = index
                        answered = true
                        if (isAnswerCorrect(question, index)) {
                            score++
                        }
                    }
                },
                onNext = {
                    if (currentQuestion < quizQuestions.size - 1) {
                        currentQuestion++
                        selectedAnswer = -1
                        answered = false
                    } else {
                        screen = "result"
                    }
                }
            )
        }

        "result" -> ResultScreen(score, quizQuestions.size) {
            screen = "welcome"
            currentQuestion = 0
            score = 0
            selectedAnswer = -1
            answered = false
        }
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAE0FF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("AndroidPedia", style = MaterialTheme.typography.headlineLarge)
        Text("¿Cuánto sabes de Android?")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Keyri Margarita Zelada Barrientos - 00154324")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onStart) {
            Text("Comenzar Quiz")
        }
    }
}

@Composable
fun QuizScreen(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    selectedAnswer: Int,
    answered: Boolean,
    onAnswerSelected: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFACF3F0))
            .padding(16.dp)
    ) {
        Text("Pregunta $questionNumber de $totalQuestions")
        Text("Puntaje: $score / $totalQuestions")

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                question.question,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        question.options.forEachIndexed { index, option ->
            val color = when {
                !answered -> Color.LightGray
                index == selectedAnswer && question.options[index] == question.correctAnswer -> Color.Green
                index == selectedAnswer && question.options[index] != question.correctAnswer -> Color.Red
                question.options[index] == question.correctAnswer -> Color.Green
                else -> Color.LightGray
            }

            Button(
                onClick = { onAnswerSelected(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = !answered,
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    disabledContainerColor = color
                )
            ) {
                Text(option)
            }
        }

        if (answered) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("💡 ${question.funFact}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNext) {
                Text(if (questionNumber == totalQuestions) "Ver Resultado" else "Siguiente")
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, total: Int, onRestart: () -> Unit) {
    val message = when (score) {
        3 -> "¡Excelente! Sabes mucho sobre Android."
        2 -> "¡Bien hecho! Tienes un buen conocimiento sobre Android."
        1 -> "Puedes mejorar, sigue aprendiendo sobre las maravillas de Android."
        else -> "Sigue intentando, tú puedes."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAE0FF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resultado Final", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Obtuviste $score de $total")
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRestart) {
            Text("Reiniciar Quiz")
        }
    }
}

fun isAnswerCorrect(question: Question, selectedIndex: Int): Boolean {
    return question.options[selectedIndex] == question.correctAnswer
}