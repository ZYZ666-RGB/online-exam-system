const DEFAULT_API_BASE = "/api";

function defaultApiBase() {
    return location.protocol === "file:" ? "http://localhost:8080/api" : DEFAULT_API_BASE;
}

function normalizeApiBase(value) {
    let text = String(value || "").trim();
    if (!text) {
        return defaultApiBase();
    }
    if (/^\d+$/.test(text)) {
        const protocol = location.protocol === "https:" ? "https:" : "http:";
        const host = location.hostname || "localhost";
        return `${protocol}//${host}:${text}/api`;
    }
    if (/^[\w.-]+:\d+(\/.*)?$/.test(text)) {
        text = `http://${text}`;
    }
    text = text.replace(/\/+$/, "");
    if (/^https?:\/\//i.test(text)) {
        try {
            const url = new URL(text);
            if (!url.pathname || url.pathname === "/") {
                url.pathname = "/api";
            }
            return url.toString().replace(/\/+$/, "");
        } catch (e) {
            return defaultApiBase();
        }
    }
    return text.startsWith("/") ? text : defaultApiBase();
}

function apiBase() {
    return normalizeApiBase(localStorage.getItem("apiBase") || defaultApiBase());
}

function saveApiBase(value) {
    const normalized = normalizeApiBase(value);
    localStorage.setItem("apiBase", normalized);
    return normalized;
}

function initApiBaseInput(id = "apiBase") {
    const el = document.getElementById(id);
    if (el) {
        el.value = apiBase();
    }
}

function token() {
    return localStorage.getItem("token") || "";
}

function saveSession(loginData) {
    localStorage.setItem("token", loginData.token);
    localStorage.setItem("user", JSON.stringify({
        userId: loginData.userId,
        username: loginData.username,
        realName: loginData.realName,
        roles: loginData.roles || []
    }));
}

function currentUser() {
    try {
        return JSON.parse(localStorage.getItem("user") || "{}");
    } catch (e) {
        return {};
    }
}

function hasRole(role) {
    const user = currentUser();
    return Array.isArray(user.roles) && user.roles.includes(role);
}

async function request(path, options = {}) {
    const headers = Object.assign({
        "Content-Type": "application/json"
    }, options.headers || {});
    if (token()) {
        headers.Authorization = token();
    }
    const response = await fetch(apiBase() + path, Object.assign({}, options, { headers }));
    const json = await response.json().catch(() => ({ code: response.status, message: "请求失败" }));
    if (json.code !== 200) {
        if (json.code === 401) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
        }
        throw new Error(json.message || "请求失败");
    }
    return json.data;
}

function setStatus(text, ok = true, id = "status") {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = text || "";
    el.className = "status " + (ok ? "ok" : "err");
}

function qs(params) {
    const search = new URLSearchParams();
    Object.keys(params || {}).forEach(key => {
        const value = params[key];
        if (value !== undefined && value !== null && value !== "") {
            search.append(key, value);
        }
    });
    const text = search.toString();
    return text ? "?" + text : "";
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;");
}

function redirectByRole() {
    if (hasRole("ADMIN")) {
        location.href = "admin-index.html";
    } else if (hasRole("TEACHER")) {
        location.href = "teacher-index.html";
    } else {
        location.href = "student-index.html";
    }
}

function requireLogin() {
    if (!token()) {
        location.href = "login.html";
    }
}

async function logout() {
    try {
        await request("/auth/logout", { method: "POST" });
    } catch (e) {
        // Local cleanup is enough for the page; backend may already have expired the token.
    }
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    location.href = "login.html";
}

function renderShell(active) {
    requireLogin();
    const user = currentUser();
    const roleChips = (user.roles || []).map(role => `<span class="role-chip">${escapeHtml(role)}</span>`).join("");
    const adminLinks = hasRole("ADMIN")
        ? `<a href="admin-index.html" class="${active === "admin" ? "active" : ""}">用户与日志</a>`
        : "";
    const teacherLinks = hasRole("TEACHER") || hasRole("ADMIN")
        ? `<a href="teacher-index.html" class="${active === "teacher" ? "active" : ""}">教师首页</a>
           <a href="question-list.html" class="${active === "questions" ? "active" : ""}">题库管理</a>
           <a href="paper-list.html" class="${active === "papers" ? "active" : ""}">试卷管理</a>
           <a href="statistics.html" class="${active === "statistics" ? "active" : ""}">成绩统计</a>`
        : "";
    const studentLinks = hasRole("STUDENT")
        ? `<a href="student-index.html" class="${active === "student" ? "active" : ""}">学生首页</a>
           <a href="exam-list.html" class="${active === "exams" ? "active" : ""}">参加考试</a>
           <a href="score-list.html" class="${active === "scores" ? "active" : ""}">我的成绩</a>
           <a href="wrong-list.html" class="${active === "wrong" ? "active" : ""}">错题本</a>`
        : "";
    document.body.insertAdjacentHTML("afterbegin", `
        <div class="layout">
            <aside class="sidebar">
                <div class="brand">在线考试系统</div>
                <nav class="nav">
                    ${adminLinks}
                    ${teacherLinks}
                    ${studentLinks}
                    <button onclick="logout()">退出登录</button>
                </nav>
            </aside>
            <main class="main">
                <div class="topbar">
                    <div class="user-summary">
                        <span>当前用户</span>
                        <span class="user-name">${escapeHtml(user.realName || user.username)}</span>
                        ${roleChips}
                    </div>
                </div>
                <div id="app"></div>
            </main>
        </div>
    `);
}
