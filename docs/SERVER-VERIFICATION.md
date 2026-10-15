# Local Ollama verification

The configured text provider reuses Termux Ollama at `http://127.0.0.1:11434`, with `llama3.2:1b` as the default model when installed.

Report these states separately:

1. **Dispatch attempted**: a command was sent to Termux. This is not proof that a process started.
2. **API responds**: the HTTP endpoint answered. This is not proof that inference works.
3. **Chat verified**: a real prompt returned non-empty generated text.

Do not install a duplicate Ollama inside proot when the Termux server is the selected provider. Offline features must remain usable without a server. If a future image or 3D provider is added, verify a decodable generated image or validated, loadable GLB/GLTF respectively; never promote health checks to job success.
