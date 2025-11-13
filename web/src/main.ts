interface FrameStats {
    fps: number;
    width: number;
    height: number;
}

class FrameViewer {
    private frameElement: HTMLImageElement;
    private fpsElement: HTMLElement;
    private resolutionElement: HTMLElement;

    constructor() {
        this.frameElement = document.getElementById('frame') as HTMLImageElement;
        this.fpsElement = document.getElementById('fps') as HTMLElement;
        this.resolutionElement = document.getElementById('resolution') as HTMLElement;

        this.loadSampleFrame();
    }

    private loadSampleFrame(): void {
        // Load a sample processed frame (base64 encoded grayscale image)
        const sampleFrameBase64 = this.generateSampleEdgeFrame();
        this.frameElement.src = `data:image/png;base64,${sampleFrameBase64}`;

        // Update stats
        const stats: FrameStats = {
            fps: 15,
            width: 640,
            height: 480
        };
        this.updateStats(stats);
    }

    private updateStats(stats: FrameStats): void {
        this.fpsElement.textContent = stats.fps.toString();
        this.resolutionElement.textContent = `${stats.width}x${stats.height}`;
    }

    private generateSampleEdgeFrame(): string {
        // Generate a simple base64 encoded PNG for demonstration
        // In a real implementation, this would load actual processed frames
        return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==";
    }
}

// Initialize the viewer when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new FrameViewer();
});
