package com.minimalnews.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Terminal Color Palettes ──────────────────────────────────────────────────

object TerminalColors {
    // Dark (default)
    val DarkBg = Color(0xFF0D1117)
    val DarkSurface = Color(0xFF161B22)
    val DarkFg = Color(0xFFC9D1D9)
    val DarkPrimary = Color(0xFF58A6FF)
    val DarkSecondary = Color(0xFF8B949E)
    val DarkAccent = Color(0xFF7EE787)
    val DarkBorder = Color(0xFF30363D)
    val DarkError = Color(0xFFF85149)
    val DarkWarning = Color(0xFFD29922)

    // Retro Green
    val GreenBg = Color(0xFF0A0A0A)
    val GreenFg = Color(0xFF00FF00)
    val GreenDim = Color(0xFF00AA00)
    val GreenBorder = Color(0xFF003300)

    // Amber
    val AmberBg = Color(0xFF1A1000)
    val AmberFg = Color(0xFFFFB000)
    val AmberDim = Color(0xFFAA7700)
    val AmberBorder = Color(0xFF332200)

    // Matrix
    val MatrixBg = Color(0xFF000000)
    val MatrixFg = Color(0xFF00FF41)
    val MatrixDim = Color(0xFF008F11)
    val MatrixBorder = Color(0xFF003B00)

    // Blue
    val BlueBg = Color(0xFF0A0E1A)
    val BlueFg = Color(0xFF00BFFF)
    val BlueDim = Color(0xFF0077AA)
    val BlueBorder = Color(0xFF002244)

    // Solarized Dark
    val SolDarkBg = Color(0xFF002B36)
    val SolDarkFg = Color(0xFF839496)
    val SolDarkPrimary = Color(0xFF268BD2)
    val SolDarkBorder = Color(0xFF073642)

    // Light
    val LightBg = Color(0xFFFFFFFF)
    val LightSurface = Color(0xFFF6F8FA)
    val LightFg = Color(0xFF24292F)
    val LightPrimary = Color(0xFF0969DA)
    val LightSecondary = Color(0xFF57606A)
    val LightBorder = Color(0xFFD0D7DE)

    // Traffic light dots
    val DotRed = Color(0xFFFF5F56)
    val DotYellow = Color(0xFFFFBD2E)
    val DotGreen = Color(0xFF27C93F)
}

data class TerminalTheme(
    val name: String,
    val displayName: String,
    val colorScheme: ColorScheme
)

val terminalThemes = listOf(
    TerminalTheme(
        "dark", "Dark",
        darkColorScheme(
            background = TerminalColors.DarkBg,
            surface = TerminalColors.DarkSurface,
            surfaceVariant = TerminalColors.DarkSurface,
            onBackground = TerminalColors.DarkFg,
            onSurface = TerminalColors.DarkFg,
            onSurfaceVariant = TerminalColors.DarkSecondary,
            primary = TerminalColors.DarkPrimary,
            onPrimary = Color.White,
            secondary = TerminalColors.DarkAccent,
            onSecondary = Color.Black,
            tertiary = TerminalColors.DarkWarning,
            error = TerminalColors.DarkError,
            outline = TerminalColors.DarkBorder,
            outlineVariant = TerminalColors.DarkBorder,
        )
    ),
    TerminalTheme(
        "retro-green", "Retro Green",
        darkColorScheme(
            background = TerminalColors.GreenBg,
            surface = Color(0xFF111111),
            surfaceVariant = Color(0xFF111111),
            onBackground = TerminalColors.GreenFg,
            onSurface = TerminalColors.GreenFg,
            onSurfaceVariant = TerminalColors.GreenDim,
            primary = TerminalColors.GreenFg,
            onPrimary = Color.Black,
            secondary = TerminalColors.GreenDim,
            onSecondary = Color.Black,
            tertiary = TerminalColors.GreenDim,
            error = Color(0xFFFF0000),
            outline = TerminalColors.GreenBorder,
            outlineVariant = TerminalColors.GreenBorder,
        )
    ),
    TerminalTheme(
        "amber", "Amber",
        darkColorScheme(
            background = TerminalColors.AmberBg,
            surface = Color(0xFF221800),
            surfaceVariant = Color(0xFF221800),
            onBackground = TerminalColors.AmberFg,
            onSurface = TerminalColors.AmberFg,
            onSurfaceVariant = TerminalColors.AmberDim,
            primary = TerminalColors.AmberFg,
            onPrimary = Color.Black,
            secondary = TerminalColors.AmberDim,
            onSecondary = Color.Black,
            tertiary = TerminalColors.AmberDim,
            error = Color(0xFFFF4444),
            outline = TerminalColors.AmberBorder,
            outlineVariant = TerminalColors.AmberBorder,
        )
    ),
    TerminalTheme(
        "matrix", "Matrix",
        darkColorScheme(
            background = TerminalColors.MatrixBg,
            surface = Color(0xFF0A0A0A),
            surfaceVariant = Color(0xFF0A0A0A),
            onBackground = TerminalColors.MatrixFg,
            onSurface = TerminalColors.MatrixFg,
            onSurfaceVariant = TerminalColors.MatrixDim,
            primary = TerminalColors.MatrixFg,
            onPrimary = Color.Black,
            secondary = TerminalColors.MatrixDim,
            onSecondary = Color.Black,
            tertiary = TerminalColors.MatrixDim,
            error = Color(0xFFFF0000),
            outline = TerminalColors.MatrixBorder,
            outlineVariant = TerminalColors.MatrixBorder,
        )
    ),
    TerminalTheme(
        "blue", "Blue",
        darkColorScheme(
            background = TerminalColors.BlueBg,
            surface = Color(0xFF0D1226),
            surfaceVariant = Color(0xFF0D1226),
            onBackground = TerminalColors.BlueFg,
            onSurface = TerminalColors.BlueFg,
            onSurfaceVariant = TerminalColors.BlueDim,
            primary = TerminalColors.BlueFg,
            onPrimary = Color.Black,
            secondary = TerminalColors.BlueDim,
            onSecondary = Color.Black,
            tertiary = TerminalColors.BlueDim,
            error = Color(0xFFFF4444),
            outline = TerminalColors.BlueBorder,
            outlineVariant = TerminalColors.BlueBorder,
        )
    ),
    TerminalTheme(
        "solarized-dark", "Solarized Dark",
        darkColorScheme(
            background = TerminalColors.SolDarkBg,
            surface = TerminalColors.SolDarkBorder,
            surfaceVariant = TerminalColors.SolDarkBorder,
            onBackground = TerminalColors.SolDarkFg,
            onSurface = TerminalColors.SolDarkFg,
            onSurfaceVariant = Color(0xFF657B83),
            primary = TerminalColors.SolDarkPrimary,
            onPrimary = Color.White,
            secondary = Color(0xFF2AA198),
            onSecondary = Color.White,
            tertiary = Color(0xFFB58900),
            error = Color(0xFFDC322F),
            outline = TerminalColors.SolDarkBorder,
            outlineVariant = TerminalColors.SolDarkBorder,
        )
    ),
    TerminalTheme(
        "light", "Light",
        lightColorScheme(
            background = TerminalColors.LightBg,
            surface = TerminalColors.LightSurface,
            surfaceVariant = TerminalColors.LightSurface,
            onBackground = TerminalColors.LightFg,
            onSurface = TerminalColors.LightFg,
            onSurfaceVariant = TerminalColors.LightSecondary,
            primary = TerminalColors.LightPrimary,
            onPrimary = Color.White,
            secondary = Color(0xFF1A7F37),
            onSecondary = Color.White,
            tertiary = Color(0xFF9A6700),
            error = Color(0xFFCF222E),
            outline = TerminalColors.LightBorder,
            outlineVariant = TerminalColors.LightBorder,
        )
    )
)

// ── Typography ───────────────────────────────────────────────────────────────

val TerminalFont = FontFamily.Monospace

val TerminalTypography = Typography(
    displayLarge = TextStyle(fontFamily = TerminalFont, fontSize = 28.sp, fontWeight = FontWeight.Bold),
    displayMedium = TextStyle(fontFamily = TerminalFont, fontSize = 24.sp, fontWeight = FontWeight.Bold),
    displaySmall = TextStyle(fontFamily = TerminalFont, fontSize = 20.sp, fontWeight = FontWeight.Bold),
    headlineLarge = TextStyle(fontFamily = TerminalFont, fontSize = 18.sp, fontWeight = FontWeight.Bold),
    headlineMedium = TextStyle(fontFamily = TerminalFont, fontSize = 16.sp, fontWeight = FontWeight.Bold),
    headlineSmall = TextStyle(fontFamily = TerminalFont, fontSize = 14.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontFamily = TerminalFont, fontSize = 16.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontFamily = TerminalFont, fontSize = 14.sp, fontWeight = FontWeight.Medium),
    titleSmall = TextStyle(fontFamily = TerminalFont, fontSize = 13.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontFamily = TerminalFont, fontSize = 14.sp),
    bodyMedium = TextStyle(fontFamily = TerminalFont, fontSize = 13.sp),
    bodySmall = TextStyle(fontFamily = TerminalFont, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = TerminalFont, fontSize = 13.sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontFamily = TerminalFont, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = TerminalFont, fontSize = 11.sp),
)

// ── Theme Composable ─────────────────────────────────────────────────────────

@Composable
fun MinimalNewsTheme(
    themeName: String = "dark",
    content: @Composable () -> Unit
) {
    val theme = terminalThemes.find { it.name == themeName } ?: terminalThemes[0]

    MaterialTheme(
        colorScheme = theme.colorScheme,
        typography = TerminalTypography,
        shapes = Shapes(
            extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(0),
            small = androidx.compose.foundation.shape.RoundedCornerShape(0),
            medium = androidx.compose.foundation.shape.RoundedCornerShape(0),
            large = androidx.compose.foundation.shape.RoundedCornerShape(0),
            extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(0),
        ),
        content = content
    )
}
