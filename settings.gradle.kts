rootProject.name = "automatic-sniffler"

include(
    "SuperStream",
    "FlixHQ",
    "Sudo-Flix",
    "Stremio",
)

rootProject.children.forEach {
    it.projectDir = file("providers/${it.name}")
}
