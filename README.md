<h1 align="center">📱 SubTrackr</h1>
<h2 align="center">Smart Subscription and Auto-Debit Alert System</h2>

<p align="center">
  <img src="https://img.shields.io/badge/platform-Android-green" alt="platform" />
  <img src="https://img.shields.io/badge/backend-SpringBoot-blue" alt="backend" />
  <img src="https://img.shields.io/badge/api-Retrofit-orange" alt="api" />
</p>

---

<h2>📝 About the Project </h2>

<p><strong>SubTrackr</strong> is an Android app designed to help users <strong>track and manage all their recurring subscriptions in one place</strong>, whether they’re paid through UPI apps like PhonePe, Google Pay, Paytm, or via direct bank auto-debit.</p>

<p>With more and more people subscribing to platforms like Netflix, Spotify, Hotstar, Amazon Prime, and other digital services, it becomes difficult to remember due dates and which app was used for payment.</p>

<p><strong>SubTrackr solves this problem</strong> by allowing users to:</p>

<ul>
  <li><strong>Manually add</strong> subscriptions with details like amount, due date, and category.</li>
  <li><strong>Automatically detect</strong> subscriptions by reading relevant SMS messages (optional).</li>
  <li><strong>Send timely alerts</strong> before auto-debits to help avoid missed payments and surprise deductions.</li>
  <li><strong>Display monthly spending summary</strong> and active subscriptions in a clean dashboard.</li>
</ul>

<p>It’s ideal for anyone who wants to stay organized, avoid late fees, and keep all their subscriptions and expenses under control — no matter how or where they pay from.</p>


---

<h2>🚀 Features</h2>

<ul>
  <li>🔔 Auto-debit alerts for upcoming due dates</li>
  <li>📅 Add/edit/delete subscriptions with category, amount, and due date</li>
  <li>📊 Monthly overview of your total subscription spending</li>
  <li>📬 SMS-based auto-detection of subscriptions (optional)</li>
  <li>📲 Clean, modern UI built with XML and Java</li>
  <li>🌐 Backend powered by Spring Boot and REST API</li>
</ul>

---

<h2>📡 How API is Called from Android App</h2>

<p>The app uses <strong>Retrofit</strong>, a type-safe HTTP client for Android, to call APIs and communicate with the backend server built using Spring Boot.</p>

<h3>✅ Steps:</h3>

<ol>
  <li>Android collects user input from the subscription form (like service name, amount, category, due date).</li>
  <li>On clicking the "Save" button, the data is passed to Retrofit.</li>
  <li>Retrofit creates a POST request to the backend API endpoint (e.g., <code>http://your-ip:8080/api/subscriptions</code>).</li>
  <li>The Spring Boot backend receives the request and stores the subscription data into the database (MySQL).</li>
  <li>On success, the app receives a confirmation response and updates the UI accordingly.</li>
</ol>

<p>📱 This connection is real-time and helps sync user data securely between phone and server.</p>

---

<h2>🛠️ Tech Stack</h2>

<ul>
  <li><strong>Frontend:</strong> Android (Java, XML)</li>
  <li><strong>Backend:</strong> Spring Boot (Java)</li>
  <li><strong>API:</strong> Retrofit (Android)</li>
  <li><strong>Database:</strong> MySQL</li>
</ul>

---

<h2>📷 Screenshots</h2>

<p align="center">
  <img src="https://github.com/user-attachments/assets/bc63b1a5-50b8-4e84-bfa5-d644e41da014" alt="Dashboard" width="30%" />
  <img src="https://github.com/user-attachments/assets/3280a7a1-8979-4017-8022-5283d6791a46" alt="Add Subscription" width="30%" />
 <img width="30%"  alt="Spending Analysis" src="https://github.com/user-attachments/assets/4354d58e-ecb0-49b7-8324-c313534dbb8f" />

</p>

<p align="center">
  <em>🟩 Dashboard View</em> &nbsp;&nbsp;&nbsp;&nbsp;
  <em>🟪 Add Subscription</em> &nbsp;&nbsp;&nbsp;&nbsp;
  <em>🟦 Spending Analysis</em>
</p>


---

<h2>📁 Project Structure</h2>

<h3>📱 Android Frontend (app/)</h3>
<ul>
  <li>📁 <strong>app/</strong> – Main Android app source</li>
  <li>📁 <strong>build/</strong> – Gradle build output</li>
  <li>📁 <strong>gradle/</strong> – Gradle wrapper files</li>
  <li>⚙️ <strong>build.gradle.kts</strong> – Gradle config (Kotlin)</li>
  <li>⚙️ <strong>settings.gradle.kts</strong> – Project settings</li>
  <li>⚙️ <strong>gradlew / gradlew.bat</strong> – Gradle wrapper scripts</li>
  <li>⚙️ <strong>.idea/, .vscode/</strong> – IDE configs (Android Studio / VSCode)</li>
  <li>⚙️ <strong>.gitignore, local.properties</strong></li>
</ul>

---

<h3>🌐 Spring Boot Backend (BackEnd/)</h3>
<ul>
  <li>📁 <strong>src/</strong> – Java source code (controllers, services, models)</li>
  <li>📁 <strong>target/</strong> – Compiled build output</li>
  <li>⚙️ <strong>pom.xml</strong> – Maven dependencies & build config</li>
  <li>⚙️ <strong>mvnw / mvnw.cmd</strong> – Maven wrapper</li>
  <li>⚙️ <strong>compose.yaml</strong> – (Optional) Docker Compose or deployment</li>
  <li>⚙️ <strong>.project, .classpath</strong> – Eclipse project files</li>
  <li>⚙️ <strong>.settings/</strong> – IDE settings</li>
  <li>⚙️ <strong>.gitignore, .gitattributes</strong></li>
</ul>


