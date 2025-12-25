const HeaderComponent = {
    currentUser: null,
    
    init: function() {
        this.loadHeader();
        this.loadTheme();
        this.initEggAnimation();
    },

    loadHeader: function() {
        const container = document.getElementById('header-container');
        if (container) {
            fetch('/components/header.html')
                .then(response => response.text())
                .then(html => {
                    container.innerHTML = html;
                    setTimeout(() => {
                        this.loadSchoolTitle();
                        this.loadUserInfo();
                    }, 200);
                })
                .catch(err => console.error('加载顶部栏失败', err));
        }
    },

    loadSchoolTitle: function() {
        const font = new FontFace('Maobi', 'url(/fonts/Maobi.ttf)');
        font.load().then(function(loadedFont) {
            document.fonts.add(loadedFont);
            const canvas = document.getElementById('school-title');
            if (canvas) {
                const ctx = canvas.getContext('2d');
                ctx.clearRect(0, 0, 150, 50);
                ctx.textAlign = 'left';
                ctx.textBaseline = 'middle';
                ctx.font = 'bold 32px Maobi';
                ctx.fillStyle = '#F59E0B';
                ctx.fillText('鸡蛋大学', 0, 25);
            }
        }).catch(e => console.error('字体加载失败', e));
    },

    loadTheme: function() {
        if (localStorage.getItem("theme") === "light") {
            document.body.classList.add("light");
        }
    },

    toggleTheme: function() {
        document.body.classList.toggle("light");
        localStorage.setItem(
            "theme",
            document.body.classList.contains("light") ? "light" : "dark"
        );
    },

    initEggAnimation: function() {
        const eggCanvas = document.getElementById('egg');
        if (eggCanvas) {
            const ctx = eggCanvas.getContext('2d');
            const egg = new Egg(eggCanvas, ctx);
            egg.animate();
        }
    },

    loadUserInfo: function() {
        const token = localStorage.getItem('token');
        console.log('Token:', token ? '存在' : '不存在');
        
        if (!token) {
            this.updateUserInfoUI(null);
            return;
        }

        console.log('开始获取用户信息...');
        apiRequest(API_ENDPOINTS.USER_INFO + `?token=${token}`)
            .then(resp => {
                console.log('API 响应状态:', resp.status);
                return resp.json();
            })
            .then(user => {
                console.log('用户数据:', user);
                this.currentUser = user;
                this.updateUserInfoUI(user);
            })
            .catch(err => {
                console.error('获取用户信息失败', err);
                this.updateUserInfoUI(null);
            });
    },

    updateUserInfoUI: function(user) {
        console.log('updateUserInfoUI 被调用, user:', user);
        
        const userInfoHeader = document.getElementById('user-info-header');
        console.log('userInfoHeader 元素:', userInfoHeader);
        
        if (!userInfoHeader) {
            console.warn('用户信息头部元素未找到，延迟重试...');
            setTimeout(() => this.updateUserInfoUI(user), 200);
            return;
        }

        if (!user) {
            console.log('没有用户数据，隐藏用户信息');
            userInfoHeader.setAttribute('data-hidden', 'true');
            return;
        }

        userInfoHeader.removeAttribute('data-hidden');
        
        const displayName = user.realName || user.username;
        console.log('显示名称:', displayName);
        
        const avatarText = document.getElementById('avatar-text');
        const userName = document.getElementById('user-name');
        const userDept = document.getElementById('user-dept');
        
        console.log('子元素:', { avatarText, userName, userDept });
        
        if (avatarText) {
            avatarText.textContent = displayName.charAt(0).toUpperCase();
        }
        
        if (userName) {
            userName.textContent = displayName;
        }

        let deptText = '';
        if (user.userType === 'STUDENT') {
            deptText = user.department || '未知院系';
        } else if (user.userType === 'TEACHER') {
            deptText = user.department || '未知院系';
        } else if (user.userType === 'ADMIN') {
            deptText = '管理员';
        }
        
        console.log('院系信息:', deptText);
        
        if (userDept) {
            userDept.textContent = deptText;
        }
    }
};
