# App de Chat en Tiempo Real

## Conceptos

`WebSocket`, Arquitectura Clean, Inyección de Dependencias, Testing

## Objetivo

Desarrollar una aplicación de chat en tiempo real con múltiples salas, usando WebSockets,
arquitectura limpia y técnicas avanzadas de Android.

## Funcionalidades Requeridas

- [ ] Autenticación de usuarios
- [ ] Lista de salas de chat
- [ ] Chat en tiempo real con WebSockets
- [ ] Envío de imágenes y archivos
- [ ] Notificaciones push
- [ ] Estados de mensaje (enviado, entregado, leído)
- [ ] Modo offline con sincronización
- [ ] Cifrado end-to-end básico

## Estructura de Archivos (Clean Architecture)

```
app/src/main/java/com/tuapp/chatapp/
├── presentation/
│ ├── ui/
│ │ ├── login/
│ │ ├── salas/
│ │ ├── chat/
│ │ └── perfil/
│ ├── viewmodel/
│ └── adapter/
├── domain/
│ ├── usecase/
│ ├── repository/
│ └── model/
├── data/
│ ├── repository/
│ ├── datasource/
│ │ ├── local/
│ │ ├── remote/
│ │ └── websocket/
│ ├── database/
│ └── network/
├── di/
└── utils/
```

## Retos Adicionales

- [ ] Implementar cifrado end-to-end
- [ ] Llamadas de voz/video con WebRTC
- [ ] Bots y comandos automatizados
- [ ] Traducciones en tiempo real
- [ ] Temas personalizables
- [ ] Métricas y analytics

## Consideraciones Personales

- Compatibilidad mínima: API 21 (Lollipop, Android 5.0)
- En
  `build.gradle.kts (:app) se deben completar``buildConfigField("String", "WEBSOCKET_API_KEY", "\"YOUR_PIESOCKET_API_KEY\"")`
  y `buildConfigField("String", "WEBSOCKET_BASE_URL", "\"wss://free.blr2.piesocket.com/v3/1\"")` con
  los puntos de acceso WebSocket a utilizar para la aplicación.
