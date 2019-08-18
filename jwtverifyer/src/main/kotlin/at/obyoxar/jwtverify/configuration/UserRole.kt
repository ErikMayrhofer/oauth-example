package at.obyoxar.jwtverify.configuration

enum class UserRole {
    USER,
    ADMIN;

    fun asAuthorityFor(user: User) =UserAuthority("ROLE_${toString()}", user)

    companion object{
        fun valueOf(auth: UserAuthority): UserRole =
            when(auth.authority){
                "ROLE_USER" -> USER
                "ROLE_ADMIN" -> ADMIN
                else -> throw IllegalArgumentException("No role defined for authority: ${auth.authority}")
            }
    }
}