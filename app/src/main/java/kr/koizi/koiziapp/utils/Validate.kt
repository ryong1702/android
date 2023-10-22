package kr.koizi.koiziapp.utils

class Validate {
    companion object {
        fun validateId(id: String): Boolean {
            val regex = Regex("^[a-z\\d]+\$")
            return matches(regex, id)
        }

        fun validatePassword(password: String): Boolean {
            val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,12}$")
            return matches(regex, password)
        }

        private fun matches(regex: Regex, input: String): Boolean {
            return regex.matches(input)
        }
    }
}