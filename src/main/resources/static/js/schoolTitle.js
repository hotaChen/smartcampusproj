// schoolTitle.js
export function renderSchoolTitle(containerId, text) {
    const container = document.getElementById(containerId);
    if (!container) return;

    const canvas = document.createElement('canvas');
    container.innerHTML = '';
    container.appendChild(canvas);
    const ctx = canvas.getContext('2d');

    const width = container.clientWidth || 400;
    const height = container.clientHeight || 100;
    canvas.width = width;
    canvas.height = height;

    const font = new FontFace('Maobi', 'url(/fonts/Maobi.ttf)');
    font.load().then(function(loadedFont) {
        document.fonts.add(loadedFont);

        ctx.clearRect(0, 0, width, height);
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.font = 'bold 52px Maobi';

        // 纯色填充
        ctx.fillStyle = '#F59E0B'; // 原登录页主题主色，金橙色
        ctx.fillText(text, width / 2, height / 2);

    }).catch(e => console.error('字体加载失败', e));
}
