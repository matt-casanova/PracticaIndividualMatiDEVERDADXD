# Gson convierte el JSON en objetos por reflexion, leyendo los NOMBRES de los campos.
# Si R8 los renombrara (a, b, c...), la app compilaria pero fallaria al leer la respuesta.
-keep class com.example.practicaindividualmati.data.remote.** { *; }
-keep class com.example.practicaindividualmati.data.ai.** { *; }
-keep class com.example.practicaindividualmati.data.kanye.** { *; }
-keep class com.example.practicaindividualmati.data.stoic.** { *; }
-keep class com.example.practicaindividualmati.models.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
