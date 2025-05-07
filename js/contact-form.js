document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("contactForm");
    const submitBtn = form.querySelector("button[type='submit']");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        // Prevent multiple submissions
        if (submitBtn.disabled) return;

        // Get form input values
        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const subject = document.getElementById("subject").value.trim();
        const message = document.getElementById("message").value.trim();

        // Simple validation
        if (!name || !email || !subject || !message) {
            alert("Please fill in all fields.");
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            alert("Please enter a valid email address.");
            return;
        }

        const contactData = { name, email, subject, message };

        // Disable button before sending
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Sending...';

        try {
            const API_BASE_URL = window.__env?.API_BASE_URL || 'http://localhost:8080';

            const response = await fetch(`${API_BASE_URL}/api/contact`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(contactData)
            });

            if (response.ok) {
                const result = await response.text();
                console.log("Success:", result);
                alert("Message sent successfully!");
                form.reset();
            } else {
                const error = await response.text();
                console.warn("Backend Error:", error);
                alert("Failed to send message: " + error);
            }
        } catch (err) {
            console.error("Fetch Error:", err);
            alert("An error occurred. Please try again later.");
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = "Send Message";
        }
    });
});
