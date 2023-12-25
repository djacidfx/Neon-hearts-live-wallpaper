package UEnginePackage.Models;

import UEnginePackage.UGL.Urect;
import UEnginePackage.Models.layers.ParticleSystem;


public class Uspawner {
    long lastTimeSpawnedPrt;
    public int maxParticles;
    public range particleLife;
    public range particlesize;
    public range positionX;
    public range positionY;
    public Urect spawningBound;
    public float sizeFactore = 1.0f;
    int timeBetweenSpawns = 50;

    public Uspawner(range rangeVar, range rangeVar2, Urect urect, int i) {
        this.particlesize = rangeVar;
        this.particleLife = rangeVar2;
        this.spawningBound = urect;
        this.maxParticles = i;
        this.positionX = new range(urect.getWidth(), 0.0d);
        this.positionY = new range(urect.getHeight(), 0.0d);
    }

    public Uparticle update(int i, ParticleSystem particleSystem, boolean z) {
        long j = this.lastTimeSpawnedPrt;
        boolean z2 = j == 0 || ((long) this.timeBetweenSpawns) + j < System.currentTimeMillis() || z;
        if ((i >= this.maxParticles || !z2) && !z) {
            return null;
        }
        this.lastTimeSpawnedPrt = System.currentTimeMillis();
        return createparticle(particleSystem);
    }

    private Uparticle createparticle(ParticleSystem particleSystem) {
        this.positionX = new range((this.spawningBound.getWidth() / 100.0d) * particleSystem.bound.getWidth(), (this.spawningBound.getLeft() / 100.0d) * particleSystem.bound.getWidth());
        this.positionY = new range((this.spawningBound.getHeight() / 100.0d) * particleSystem.bound.getHeight(), (this.spawningBound.getTop() / 100.0d) * particleSystem.bound.getHeight());
        double randomValue = this.particlesize.getRandomValue() * particleSystem.bound.getWidth();
        double d = this.sizeFactore;
        Double.isNaN(d);
        double d2 = randomValue * d;
        double d3 = d2 / 2.0d;
        return Uparticle.obtain(this.positionX.getRandomValue() - d3, this.positionY.getRandomValue() - d3, d2, d2, particleSystem.getRandomImage(), (int) this.particleLife.getRandomValue());
    }

    public Uspawner m0clone() {
        return new Uspawner(this.particlesize, this.particleLife, this.spawningBound, this.maxParticles);
    }
}
