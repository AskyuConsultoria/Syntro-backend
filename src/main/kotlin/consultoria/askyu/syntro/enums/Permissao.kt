package consultoria.askyu.syntro.enums

enum class Permissao(val codigo: Int) {
    GESTOR(1),
    AUDITOR(2),
    EMISSOR(3);

    companion object {
        fun fromCodigo(codigo: Int): Permissao? {
            return values().find { it.codigo == codigo }
        }
    }
}