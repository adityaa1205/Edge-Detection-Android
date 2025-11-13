"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class FrameViewer {
    frameElement;
    fpsElement;
    resolutionElement;
    stats = { fps: 0, width: 0, height: 0 };
    frameCount = 0;
    lastTime = Date.now();
    constructor() {
        this.frameElement = document.getElementById('frame');
        this.fpsElement = document.getElementById('fps');
        this.resolutionElement = document.getElementById('resolution');
        this.loadSampleFrame();
        this.updateStats();
    }
    loadSampleFrame() {
        // For demonstration, create a simple sample frame
        // In a real app, this would come from the Android app
        const canvas = document.createElement('canvas');
        canvas.width = 320;
        canvas.height = 240;
        const ctx = canvas.getContext('2d');
        // Create a simple edge-like pattern
        ctx.fillStyle = 'black';
        ctx.fillRect(0, 0, 320, 240);
        ctx.strokeStyle = 'white';
        ctx.lineWidth = 2;
        ctx.strokeRect(10, 10, 300, 220);
        ctx.beginPath();
        ctx.moveTo(160, 10);
        ctx.lineTo(160, 230);
        ctx.stroke();
        this.frameElement.src = canvas.toDataURL();
        this.stats.width = 320;
        this.stats.height = 240;
    }
    updateStats() {
        this.frameCount++;
        const now = Date.now();
        if (now - this.lastTime >= 1000) {
            this.stats.fps = Math.round((this.frameCount * 1000) / (now - this.lastTime));
            this.frameCount = 0;
            this.lastTime = now;
        }
        this.fpsElement.textContent = this.stats.fps.toString();
        this.resolutionElement.textContent = `${this.stats.width}x${this.stats.height}`;
    }
}
// Initialize the viewer when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new FrameViewer();
});
//# sourceMappingURL=main.js.map