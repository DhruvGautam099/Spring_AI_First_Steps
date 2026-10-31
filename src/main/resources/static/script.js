// ---------- Tabs ----------
document.querySelectorAll(".tab").forEach(function (tab) {
    tab.addEventListener("click", function () {
        document.querySelectorAll(".tab").forEach(function (t) { t.classList.remove("active"); });
        document.querySelectorAll(".panel").forEach(function (p) { p.classList.remove("active"); });
        tab.classList.add("active");
        document.getElementById(tab.dataset.target).classList.add("active");
    });
});

// ---------- Helpers ----------
function setStatus(id, message, isError) {
    var el = document.getElementById(id);
    el.textContent = message;
    el.classList.toggle("error", !!isError);
}

async function readError(response) {
    var text = "";
    try {
        text = await response.text();
    } catch (e) { /* ignore */ }
    return "Error " + response.status + (text ? ": " + text : "");
}

// ---------- Chat (/ai/openAi/generate and /ai/ollama/generate) ----------
var chatBtn = document.getElementById("chat-send");

chatBtn.addEventListener("click", async function () {
    var prompt = document.getElementById("chat-prompt").value.trim();
    var url = document.getElementById("provider").value;
    var output = document.getElementById("chat-output");

    if (!prompt) {
        setStatus("chat-status", "Please enter a prompt.", true);
        return;
    }

    chatBtn.disabled = true;
    output.textContent = "";
    setStatus("chat-status", "Waiting for response...");

    try {
        // Controller uses @RequestBody String, so send plain text
        var response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "text/plain" },
            body: prompt
        });

        if (!response.ok) {
            setStatus("chat-status", await readError(response), true);
        } else {
            output.textContent = await response.text();
            setStatus("chat-status", "");
        }
    } catch (err) {
        setStatus("chat-status", "Request failed: " + err.message, true);
    } finally {
        chatBtn.disabled = false;
    }
});

// ---------- Image (/api/image/describe) ----------
var imageBtn = document.getElementById("image-send");
var imageInput = document.getElementById("image-file");
var imagePreview = document.getElementById("image-preview");

imageInput.addEventListener("change", function () {
    var file = imageInput.files[0];
    if (file) {
        imagePreview.src = URL.createObjectURL(file);
        imagePreview.hidden = false;
    } else {
        imagePreview.hidden = true;
    }
});

imageBtn.addEventListener("click", async function () {
    var file = imageInput.files[0];
    var question = document.getElementById("image-question").value.trim();
    var output = document.getElementById("image-output");

    if (!file) {
        setStatus("image-status", "Please choose an image.", true);
        return;
    }
    if (!question) {
        setStatus("image-status", "Please enter a question.", true);
        return;
    }

    var formData = new FormData();
    formData.append("file", file);
    formData.append("question", question);

    imageBtn.disabled = true;
    output.textContent = "";
    setStatus("image-status", "Analyzing image...");

    try {
        // Do not set Content-Type manually; browser adds the multipart boundary
        var response = await fetch("/api/image/describe", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            setStatus("image-status", await readError(response), true);
        } else {
            output.textContent = await response.text();
            setStatus("image-status", "");
        }
    } catch (err) {
        setStatus("image-status", "Request failed: " + err.message, true);
    } finally {
        imageBtn.disabled = false;
    }
});

// ---------- Voice (/api/voice/ask) ----------
var voiceBtn = document.getElementById("voice-send");
var audioEl = document.getElementById("voice-audio");

voiceBtn.addEventListener("click", async function () {
    var question = document.getElementById("voice-question").value.trim();
    var voice = document.getElementById("voice-name").value.trim();
    var output = document.getElementById("voice-output");

    if (!question) {
        setStatus("voice-status", "Please enter a question.", true);
        return;
    }

    voiceBtn.disabled = true;
    output.textContent = "";
    audioEl.hidden = true;
    setStatus("voice-status", "Generating answer and audio...");

    try {
        var response = await fetch("/api/voice/ask", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ question: question, voice: voice || null })
        });

        if (!response.ok) {
            setStatus("voice-status", await readError(response), true);
            return;
        }

        // Answer text comes in the X-Answer-Text header (URL-encoded by the server)
        var encoded = response.headers.get("X-Answer-Text");
        if (encoded) {
            output.textContent = decodeURIComponent(encoded.replace(/\+/g, " "));
        }

        var blob = await response.blob();
        if (audioEl.src) {
            URL.revokeObjectURL(audioEl.src);
        }
        audioEl.src = URL.createObjectURL(blob);
        audioEl.hidden = false;
        audioEl.play().catch(function () { /* autoplay may be blocked; user can press play */ });

        setStatus("voice-status", "");
    } catch (err) {
        setStatus("voice-status", "Request failed: " + err.message, true);
    } finally {
        voiceBtn.disabled = false;
    }
});