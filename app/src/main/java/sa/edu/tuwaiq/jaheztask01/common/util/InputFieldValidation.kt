package sa.edu.tuwaiq.jaheztask01.common.util

import java.util.regex.Pattern

object InputFieldValidation {
    // Password regular expression
    private val REGEX_PASSWORD = "^(?=.*[0-9])" +  // a digit must occur at least once
            "(?=.*[a-z])" +  // a lower case letter must occur at least once
            "(?=.*[A-Z])" +  // an upper case letter must occur at least once
            "(?=\\S+$)" +  // no whitespace allowed in the entire string
            ".{8,}$" // anything, at least eight places though

    // Email regular expression
    private val REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"

    // Check the email
    fun emailsIsValid(email: String): Boolean {
        val pattern = Pattern.compile(REGEX_EMAIL)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    // Check the password
    fun passwordIsValid(password: String): Boolean {
        val pattern = Pattern.compile(REGEX_PASSWORD)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}