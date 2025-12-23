(() => {
    let animationId = null;
    let angle = 0; // 呼吸动画

    function initEgg() {
        const egg = document.getElementById("egg");
        if (!egg || !egg.getContext) {
            setTimeout(initEgg, 50);
            return;
        }

        const ctx = egg.getContext("2d");

        const rect = egg.getBoundingClientRect();
        const dpr = window.devicePixelRatio || 1;
        egg.width = rect.width * dpr;
        egg.height = rect.height * dpr;
        ctx.scale(dpr, dpr);

        const centerX = rect.width / 2;
        const centerY = rect.height / 2;
        let mouse = { x: centerX, y: centerY };

        window.addEventListener("mousemove", e => {
            const rect = egg.getBoundingClientRect();
            mouse.x = e.clientX - rect.left;
            mouse.y = e.clientY - rect.top;
        });

        function drawEgg() {
            ctx.clearRect(0, 0, rect.width, rect.height);
            ctx.save();

            // 呼吸动画
            const floatOffset = Math.sin(angle) * 3;
            angle += 0.05;

            // 阴影
            const shadowOffsetX = Math.max(-6, Math.min((mouse.x - centerX) * 0.1, 6));
            const shadowOffsetY = Math.max(2, Math.min((mouse.y - centerY) * 0.05 + 3, 8));
            ctx.beginPath();
            ctx.ellipse(
                centerX + shadowOffsetX,
                centerY + 45 + shadowOffsetY * 0.5 + floatOffset,
                26 - Math.abs(shadowOffsetX) * 0.2, // 阴影水平略大
                7 + shadowOffsetY * 0.2,            // 阴影垂直略大
                0, 0, Math.PI * 2
            );
            const shadowAlpha = Math.max(0.15, Math.min(0.35, 1 - (mouse.y / rect.height) * 0.4));
            ctx.fillStyle = `rgba(0,0,0,${shadowAlpha})`;
            ctx.fill();

            // 高光
            const gx = Math.max(centerX - 15, Math.min(mouse.x, centerX + 15));
            const gy = Math.max(centerY - 25, Math.min(mouse.y, centerY + 15));
            const grad = ctx.createRadialGradient(gx, gy, 2, centerX, centerY + floatOffset, 40);
            grad.addColorStop(0, "#fff8e1");
            grad.addColorStop(0.3, "#ffefb3");
            grad.addColorStop(0.6, "#ffe08a");
            grad.addColorStop(1, "#fbc02d");

            // 鸡蛋主体 - 上瘦下胖
            ctx.beginPath();
            ctx.save();
            // 上移一点，让上瘦，下胖更明显
            ctx.translate(centerX, centerY + floatOffset);
            // scaleX=1 保持水平不变, scaleY < 1 上部瘦, >1 下部胖
            ctx.scale(1, 1.4);
            ctx.ellipse(0, -5, 28, 25, 0, 0, Math.PI * 2); // -5 让上尖
            ctx.restore();

            ctx.fillStyle = grad;
            ctx.strokeStyle = "rgba(240,180,50,0.1)";
            ctx.lineWidth = 0.5;
            ctx.fill();
            ctx.stroke();

            ctx.restore();
            animationId = requestAnimationFrame(drawEgg);
        }

        if (animationId) cancelAnimationFrame(animationId);
        drawEgg();
    }

    window.addEventListener("load", initEgg);
})();
