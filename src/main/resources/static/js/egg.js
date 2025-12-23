(() => {
    let animationId = null;

    function initEgg() {
        const egg = document.getElementById("egg");
        if (!egg || !egg.getContext) {
            setTimeout(initEgg, 50);
            return;
        }

        const ctx = egg.getContext("2d");

        // 获取显示尺寸 & Retina 缩放
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

            // 阴影
            const shadowOffsetX = Math.max(-6, Math.min((mouse.x - centerX) * 0.1, 6));
            const shadowOffsetY = Math.max(2, Math.min((mouse.y - centerY) * 0.05 + 3, 8));
            ctx.beginPath();
            ctx.ellipse(
                centerX + shadowOffsetX,
                centerY + 40 + shadowOffsetY * 0.5,
                24 - Math.abs(shadowOffsetX) * 0.2,
                5 + shadowOffsetY * 0.2,
                0, 0, Math.PI * 2
            );
            const shadowAlpha = Math.max(0.15, Math.min(0.35, 1 - (mouse.y / rect.height) * 0.4));
            ctx.fillStyle = `rgba(0,0,0,${shadowAlpha})`;
            ctx.fill();

            // 高光
            const gx = Math.max(centerX - 15, Math.min(mouse.x, centerX + 15));
            const gy = Math.max(centerY - 20, Math.min(mouse.y, centerY + 20));
            const grad = ctx.createRadialGradient(gx, gy, 2, centerX, centerY, 40);
            grad.addColorStop(0, "#fff8e1");
            grad.addColorStop(0.3, "#ffefb3");
            grad.addColorStop(0.6, "#ffe08a");
            grad.addColorStop(1, "#fbc02d");

            // 鸡蛋主体
            ctx.beginPath();
            ctx.ellipse(centerX, centerY, 30, 40, 0, 0, Math.PI * 2);
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
