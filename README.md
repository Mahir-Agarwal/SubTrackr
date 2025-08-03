<h1 align="center">📱 SubTrackr</h1>
<h3 align="center">Smart Subscription and Auto-Debit Alert System</h3>

<p align="center">
  <img src="https://img.shields.io/badge/platform-Android-green" alt="platform" />
  <img src="https://img.shields.io/badge/backend-SpringBoot-blue" alt="backend" />
  <img src="https://img.shields.io/badge/api-Retrofit-orange" alt="api" />
</p>

---

<h2>📝 About the Project</h2>

<p><strong>SubTrackr</strong> is an Android app that helps users manage and track their recurring subscriptions (like Netflix, Spotify, Amazon Prime, etc.). It also sends timely alerts before auto-debit happens, helping users avoid surprise deductions.</p>

<p>This app is ideal for people who have multiple monthly/yearly subscriptions and want a clean, central way to monitor them.</p>

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

<p><em>📌 Add screenshots here of your Dashboard, Add Subscription screen, etc.</em></p>

---

<h2>📁 Project Structure (Android)</h2>
<ul>
  <li>📦 <strong>SubTrackr</strong>
    <ul>
      <li>📂 <strong>activities/</strong>
        <ul>
          <li>📄 AddSub.java</li>
          <li>📄 Dashboard.java</li>
        </ul>
      </li>
      <li>📂 <strong>models/</strong>
        <ul>
          <li>📄 Subscription.java</li>
        </ul>
      </li>
      <li>📂 <strong>api/</strong>
        <ul>
          <li>📄 ApiClient.java</li>
          <li>📄 ApiInterface.java</li>
        </ul>
      </li>
      <li>📂 <strong>layout/</strong>
        <ul>
          <li>📄 activity_add_sub.xml</li>
          <li>📄 activity_dashboard.xml</li>
        </ul>
      </li>
      <li>📄 AndroidManifest.xml</li>
    </ul>
  </li>
</ul>

