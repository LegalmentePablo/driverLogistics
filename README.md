# Driver Logistics App

Aplicacion Android de prueba tecnica orientada a entregas con enfoque offline-first.

## Arquitectura

Se implementa una arquitectura por capas con separacion de responsabilidades:

- core: utilidades transversales (conectividad, constantes de sincronizacion).
- domain: modelos, contratos de repositorio y casos de uso.
- data: Room, mappers, repositorio offline-first, fuente remota fake y sincronizacion.
- ui: pantallas Compose, estados de UI y ViewModels.
- worker: sincronizacion en segundo plano con WorkManager.

Patrones usados:

- Clean Architecture para mantener dominio desacoplado.
- MVVM para pantalla Home y Detail.
- Flow/StateFlow para UI reactiva.

## Decisiones Offline-First

- Fuente de verdad local: Room.
- Actualizacion optimista: al marcar entregado, la UI y Room cambian de inmediato.
- Sin internet: la accion se encola en pending_sync_actions.
- Con internet: Worker procesa la cola y elimina acciones sincronizadas.
- Reapertura de app: se preserva estado local Delivered al refrescar desde remoto.
- Reconexion: se asegura un work one-time unico para sync, evitando duplicados.

## Como Probar Escenario Modo Avion

1. Abrir la app con internet y esperar carga inicial de entregas.
2. Entrar a detalle de una entrega Pendiente.
3. Activar modo avion (o desactivar datos/Wi-Fi).
4. Tocar Marcar como entregado.
5. Verificar que la UI cambia a Entregado de forma inmediata y muestra mensaje de cola.
6. Desactivar modo avion.
7. Mantenerse en la pantalla de detalle y verificar que se limpia el estado pendiente tras sincronizar.

Tip de QA manual:

- Usar el boton Reset DEV estados en Home para restaurar estados iniciales y repetir pruebas.

## Ejecutar Pruebas

Ejecutar pruebas unitarias del modulo app:

- Windows (PowerShell):
	- .\gradlew.bat :app:testDebugUnitTest

- Linux/macOS:
	- ./gradlew :app:testDebugUnitTest

## Cobertura de pruebas agregadas

- Caso de uso critico: marcar entregado en offline devuelve estado encolado.
- Caso de uso critico: observacion de cola pendiente para una entrega.
- ViewModel: DetailViewModel valida actualizacion optimista + estado queued en offline.

## Entregables

- Carpeta Drive (video + APK): https://drive.google.com/drive/folders/1y1xcElhgWTlX7awfS5inZ6ceBgu6AiKL?usp=sharing

## Uso de Inteligencia Artificial

Para esta prueba tecnica se utilizo apoyo de Inteligencia Artificial.

Modelo utilizado:

- GPT-5.3-Codex (GitHub Copilot).

Como se aplico en la solucion:

- Se uso para proponer estructura inicial por capas (domain, data, ui, worker, core).
- Se uso para acelerar implementaciones puntuales de Room, WorkManager y flujo offline-first.
- Se uso para iterar mejoras de UI en Compose y ajustes de textos.
- Se uso para generar y refinar pruebas unitarias y documentacion del README.

Toda la logica final fue revisada, validada y ajustada manualmente, incluyendo pruebas de compilacion y escenarios offline/online.