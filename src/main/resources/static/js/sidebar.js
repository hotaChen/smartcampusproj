// ===== 共享侧边栏模块 =====

// 侧边栏配置
const sidebarConfig = {
    isCollapsed: false,
    currentPage: ''
};

// 页面跳转映射
const pageMappings = {
    'appointment': '../classroom/appointment.html',
    'register': '../auth/register.html',
    'classroom': '../classroom/classroom.html',
    'course': '../course/course.html',
    'courseselection': '../course/courseselection.html',
    'timetable': '../timetable/timetable.html',
    'teachertimetable': '../timetable/teachertimetable.html',
    'studenttimetable': '../timetable/studenttimetable.html',
    'syllabus': '../course/syllabus.html',
    'grade': '../grade/grade-index.html',
    'finance': '../finance/finance-index.html'
};

// SVG图标模板
const icons = {
    calendar: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>`,
    building: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 21h18M3 7v14M21 7v14M6 3v4M18 3v4M6 7V3M18 7V3"/></svg>`,
    book: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>`,
    clipboard: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>`,
    chart: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>`,
    wallet: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>`,
    lock: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>`,
    user: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>`,
    refresh: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg>`,
    edit: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>`
};

// 侧边栏HTML模板
function getSidebarHTML() {
    return `
        <!-- 侧边栏 -->
        <div class="sidebar" id="sidebar">
            <!-- 侧边栏切换按钮 -->
            <button class="sidebar-toggle" id="sidebarToggle">&#9776;</button>
            
            <!-- 侧边栏收起状态显示的图标 -->
            <div class="sidebar-collapsed-icon" id="sidebarCollapsedIcon">&#9776;</div>
            
            <!-- 侧边栏头部 -->
            <div class="sidebar-header">
                <canvas id="sidebar-egg" width="40" height="50" class="sidebar-logo"></canvas>
            </div>
            
            <!-- 侧边栏内容 -->
            <div class="sidebar-content">
                <!-- 常用功能 -->
                <div class="sidebar-group" id="favoritesGroup">
                    <div class="sidebar-group-title">
                        <span>常用功能</span>
                        <span class="disclosure">&#9660;</span>
                    </div>
                    <div class="sidebar-group-items">
                        <ul class="sidebar-menu">
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="appointment">
                                    <span class="sidebar-menu-icon">${icons.calendar}</span>
                                    <span class="sidebar-menu-text">预约模块</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="classroom">
                                    <span class="sidebar-menu-icon">${icons.building}</span>
                                    <span class="sidebar-menu-text">教室系统</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="course">
                                    <span class="sidebar-menu-icon">${icons.book}</span>
                                    <span class="sidebar-menu-text">课程管理</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                
                <!-- 系统管理 -->
                <div class="sidebar-group" id="systemGroup">
                    <div class="sidebar-group-title">
                        <span>系统管理</span>
                        <span class="disclosure">&#9660;</span>
                    </div>
                    <div class="sidebar-group-items">
                        <ul class="sidebar-menu">
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="timetable">
                                    <span class="sidebar-menu-icon">${icons.calendar}</span>
                                    <span class="sidebar-menu-text">排课系统</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="syllabus">
                                    <span class="sidebar-menu-icon">${icons.clipboard}</span>
                                    <span class="sidebar-menu-text">课程大纲</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="grade">
                                    <span class="sidebar-menu-icon">${icons.chart}</span>
                                    <span class="sidebar-menu-text">成绩管理</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" data-page="finance">
                                    <span class="sidebar-menu-icon">${icons.wallet}</span>
                                    <span class="sidebar-menu-text">财务管理</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                
                <!-- 用户功能 -->
                <div class="sidebar-group" id="userGroup">
                    <div class="sidebar-group-title">
                        <span>用户功能</span>
                        <span class="disclosure">&#9660;</span>
                    </div>
                    <div class="sidebar-group-items">
                        <ul class="sidebar-menu">
                            <li class="sidebar-menu-item">
                                <a href="#" class="sidebar-menu-link" id="sidebarChangePassword">
                                    <span class="sidebar-menu-icon">${icons.lock}</span>
                                    <span class="sidebar-menu-text">修改密码</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item admin-only">
                                <a href="#" class="sidebar-menu-link" data-page="register">
                                    <span class="sidebar-menu-icon">${icons.user}</span>
                                    <span class="sidebar-menu-text">注册新用户</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item admin-only">
                                <a href="#" class="sidebar-menu-link" id="sidebarResetPassword">
                                    <span class="sidebar-menu-icon">${icons.refresh}</span>
                                    <span class="sidebar-menu-text">重置用户密码</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item student-only">
                                <a href="#" class="sidebar-menu-link" data-page="courseselection">
                                    <span class="sidebar-menu-icon">${icons.edit}</span>
                                    <span class="sidebar-menu-text">学生选课系统</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item student-only">
                                <a href="#" class="sidebar-menu-link" data-page="studenttimetable">
                                    <span class="sidebar-menu-icon">${icons.calendar}</span>
                                    <span class="sidebar-menu-text">学生课表</span>
                                </a>
                            </li>
                            <li class="sidebar-menu-item teacher-only">
                                <a href="#" class="sidebar-menu-link" data-page="teachertimetable">
                                    <span class="sidebar-menu-icon">${icons.calendar}</span>
                                    <span class="sidebar-menu-text">教师课表</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 修改密码弹窗 -->
        <div class="modal" id="changePasswordModal">
            <div class="modal-content card">
                <h3>修改密码</h3>
                <input type="password" id="sidebarOldPassword" placeholder="旧密码" class="input"><br>
                <input type="password" id="sidebarNewPassword" placeholder="新密码" class="input"><br>
                <button class="btn" onclick="SidebarManager.submitChangePassword()">提交</button>
                <button class="btn" onclick="SidebarManager.closeChangePassword()" style="margin-top: 10px; background: rgba(255,255,255,0.2);">取消</button>
                <p id="sidebarChangePasswordMsg"></p>
            </div>
        </div>
        
        <!-- 管理员重置密码弹窗 -->
        <div class="modal" id="resetPasswordModal">
            <div class="modal-content card">
                <h3>重置用户密码（管理员）</h3>
                <input type="text" id="sidebarResetUsername" placeholder="用户名" class="input"><br>
                <input type="password" id="sidebarResetNewPassword" placeholder="新密码" class="input"><br>
                <button class="btn" onclick="SidebarManager.submitResetPassword()">提交</button>
                <button class="btn" onclick="SidebarManager.closeResetPassword()" style="margin-top: 10px; background: rgba(255,255,255,0.2);">取消</button>
                <p id="sidebarResetPasswordMsg"></p>
            </div>
        </div>
    `;
}

// 侧边栏管理器
const SidebarManager = {
    currentUser: null,
    token: null,
    
    init: function(options = {}) {
        this.token = localStorage.getItem('token');
        this.currentPage = options.currentPage || '';
        
        document.body.insertAdjacentHTML('afterbegin', getSidebarHTML());
        
        this.bindEvents();
        
        this.restoreSidebarState();
        
        this.loadUserInfo();
        
        this.highlightCurrentPage();
    },
    
    bindEvents: function() {
        const sidebarToggle = document.getElementById('sidebarToggle');
        const sidebarCollapsedIcon = document.getElementById('sidebarCollapsedIcon');
        const sidebar = document.getElementById('sidebar');
        
        const toggleSidebar = () => {
            sidebar.classList.toggle('collapsed');
            const icon = sidebar.classList.contains('collapsed') ? '&#9776;' : '&#10005;';
            sidebarToggle.innerHTML = icon;
            sidebarCollapsedIcon.innerHTML = icon;
            
            localStorage.setItem('sidebarCollapsed', sidebar.classList.contains('collapsed'));
        };
        
        sidebarToggle.addEventListener('click', toggleSidebar);
        sidebarCollapsedIcon.addEventListener('click', toggleSidebar);
        
        document.querySelectorAll('.sidebar-group-title').forEach(title => {
            title.addEventListener('click', () => {
                const group = title.parentElement;
                group.classList.toggle('collapsed');
                
                const groupId = group.id;
                localStorage.setItem(groupId + 'Collapsed', group.classList.contains('collapsed'));
            });
        });
        
        document.querySelectorAll('.sidebar-menu-link[data-page]').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = link.dataset.page;
                if (pageMappings[page]) {
                    window.location.href = pageMappings[page];
                }
            });
        });
        
        const changePasswordLink = document.getElementById('sidebarChangePassword');
        if (changePasswordLink) {
            changePasswordLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.openChangePassword();
            });
        }
        
        const resetPasswordLink = document.getElementById('sidebarResetPassword');
        if (resetPasswordLink) {
            resetPasswordLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.openResetPassword();
            });
        }
    },
    
    restoreSidebarState: function() {
        const sidebar = document.getElementById('sidebar');
        const sidebarToggle = document.getElementById('sidebarToggle');
        const sidebarCollapsedIcon = document.getElementById('sidebarCollapsedIcon');
        
        if (localStorage.getItem('sidebarCollapsed') === 'true') {
            sidebar.classList.add('collapsed');
            sidebarToggle.innerHTML = '&#9776;';
            sidebarCollapsedIcon.innerHTML = '&#9776;';
        }
        
        document.querySelectorAll('.sidebar-group').forEach(group => {
            const groupId = group.id;
            if (localStorage.getItem(groupId + 'Collapsed') === 'true') {
                group.classList.add('collapsed');
            }
        });
    },
    
    loadUserInfo: function() {
        if (!this.token) {
            return;
        }
        
        apiRequest(API_ENDPOINTS.USER_INFO + `?token=${this.token}`)
            .then(resp => resp.json())
            .then(user => {
                this.currentUser = user;
                this.updateSidebarMenu(user.userType);
            })
            .catch(err => {
                console.error('加载用户信息失败', err);
            });
    },
    
    updateSidebarMenu: function(userType) {
        document.querySelectorAll('.admin-only, .student-only, .teacher-only').forEach(item => {
            item.style.display = 'none';
        });
        
        if (userType === 'ADMIN') {
            document.querySelectorAll('.admin-only').forEach(item => {
                item.style.display = 'block';
            });
        } else if (userType === 'STUDENT') {
            document.querySelectorAll('.student-only').forEach(item => {
                item.style.display = 'block';
            });
        } else if (userType === 'TEACHER') {
            document.querySelectorAll('.teacher-only').forEach(item => {
                item.style.display = 'block';
            });
        }
    },
    
    highlightCurrentPage: function() {
        document.querySelectorAll('.sidebar-menu-link[data-page]').forEach(link => {
            const page = link.dataset.page;
            if (pageMappings[page]) {
                const currentPath = window.location.pathname;
                if (currentPath.includes(pageMappings[page])) {
                    link.classList.add('active');
                }
            }
        });
    },
    
    drawSidebarEgg: function() {
        const canvas = document.getElementById('sidebar-egg');
        if (canvas) {
            const ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, 40, 50);
            
            ctx.beginPath();
            ctx.moveTo(20, 5);
            ctx.bezierCurveTo(35, 5, 40, 20, 40, 30);
            ctx.bezierCurveTo(40, 40, 35, 45, 20, 45);
            ctx.bezierCurveTo(5, 45, 0, 40, 0, 30);
            ctx.bezierCurveTo(0, 20, 5, 5, 20, 5);
            ctx.closePath();
            
            const gradient = ctx.createLinearGradient(0, 0, 0, 50);
            gradient.addColorStop(0, '#fde68a');
            gradient.addColorStop(1, '#f59e0b');
            ctx.fillStyle = gradient;
            ctx.fill();
            
            ctx.strokeStyle = '#d97706';
            ctx.lineWidth = 1;
            ctx.stroke();
        }
    },
    
    openChangePassword: function() {
        if (!this.currentUser) {
            this.showToast('请先登录');
            return;
        }
        document.getElementById('changePasswordModal').style.display = 'flex';
    },
    
    closeChangePassword: function() {
        document.getElementById('changePasswordModal').style.display = 'none';
        document.getElementById('sidebarChangePasswordMsg').innerText = '';
        document.getElementById('sidebarOldPassword').value = '';
        document.getElementById('sidebarNewPassword').value = '';
    },
    
    submitChangePassword: function() {
        const oldPwd = document.getElementById('sidebarOldPassword').value;
        const newPwd = document.getElementById('sidebarNewPassword').value;
        
        if (!this.currentUser) {
            this.showToast('用户信息未加载');
            return;
        }
        
        apiRequest(`${API_ENDPOINTS.CHANGE_PASSWORD}?userId=${this.currentUser.id}&oldPassword=${oldPwd}&newPassword=${newPwd}`, {
            method: 'POST'
        })
            .then(resp => resp.text())
            .then(msg => {
                document.getElementById('sidebarChangePasswordMsg').innerText = msg;
                this.showToast(msg);
                if (msg.includes('成功')) {
                    this.closeChangePassword();
                }
            })
            .catch(err => {
                document.getElementById('sidebarChangePasswordMsg').innerText = '修改失败';
                this.showToast('修改失败');
                console.error(err);
            });
    },
    
    openResetPassword: function() {
        if (!this.currentUser || this.currentUser.userType !== 'ADMIN') {
            this.showToast('无权限访问');
            return;
        }
        document.getElementById('resetPasswordModal').style.display = 'flex';
    },
    
    closeResetPassword: function() {
        document.getElementById('resetPasswordModal').style.display = 'none';
        document.getElementById('sidebarResetPasswordMsg').innerText = '';
        document.getElementById('sidebarResetUsername').value = '';
        document.getElementById('sidebarResetNewPassword').value = '';
    },
    
    submitResetPassword: function() {
        if (!this.currentUser || this.currentUser.userType !== 'ADMIN') {
            this.showToast('无权限访问');
            return;
        }
        
        const username = document.getElementById('sidebarResetUsername').value;
        const newPwd = document.getElementById('sidebarResetNewPassword').value;
        
        apiRequest(`${API_ENDPOINTS.RESET_PASSWORD}?username=${username}&newPassword=${newPwd}`, {
            method: 'POST'
        })
            .then(resp => resp.text())
            .then(msg => {
                document.getElementById('sidebarResetPasswordMsg').innerText = msg;
                this.showToast(msg);
                if (msg.includes('成功')) {
                    this.closeResetPassword();
                }
            })
            .catch(err => {
                document.getElementById('sidebarResetPasswordMsg').innerText = '重置失败';
                this.showToast('重置失败');
                console.error(err);
            });
    },
    
    showToast: function(msg) {
        let toast = document.getElementById('sidebarToast');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'sidebarToast';
            toast.className = 'toast';
            document.body.appendChild(toast);
        }
        toast.textContent = msg;
        toast.classList.add('show');
        setTimeout(() => toast.classList.remove('show'), 2500);
    }
};

document.addEventListener('DOMContentLoaded', function() {
    setTimeout(() => {
        SidebarManager.drawSidebarEgg();
    }, 100);
});

window.SidebarManager = SidebarManager;
