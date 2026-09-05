package com.example.ipdapplication.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipdapplication.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loggedInUserId: Long? = null,
    val loggedInRole: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val tokenStore: TokenStore     // 🔥 TokenStore 주입됨
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun onEmailChange(v: String) = _state.update { it.copy(email = v) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v) }
    fun onNameChange(v: String) = _state.update { it.copy(name = v) }

    fun clearError() = _state.update { it.copy(error = null) }

    fun login(onSuccess: (userId: Long, role: String) -> Unit) {
        val cur = state.value
        if (cur.email.isBlank() || cur.password.isBlank()) {
            _state.update { it.copy(error = "이메일과 비밀번호를 입력하세요.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repo.login(cur.email, cur.password)
                .onSuccess { resp ->

                    // 🔥🔥🔥 반드시 있어야 하는 핵심 코드
                    resp.token?.let { token ->
                        tokenStore.saveToken(token)
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            loggedInUserId = resp.userId,
                            loggedInRole = resp.role
                        )
                    }

                    onSuccess(resp.userId, resp.role)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "로그인에 실패했습니다."
                        )
                    }
                }
        }
    }

    fun signUp(onSuccess: () -> Unit) {
        val cur = state.value
        if (cur.email.isBlank() || cur.password.isBlank() || cur.name.isBlank()) {
            _state.update { it.copy(error = "이메일, 비밀번호, 이름을 모두 입력하세요.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repo.signUp(cur.email, cur.password, cur.name)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "회원가입에 실패했습니다."
                        )
                    }
                }
        }
    }
}
