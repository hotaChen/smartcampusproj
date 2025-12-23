const HeaderComponent = {
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
                    setTimeout(() => this.loadSchoolTitle(), 100);
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
    }
};
