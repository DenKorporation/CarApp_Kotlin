package by.bsuir.carapp.states

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)