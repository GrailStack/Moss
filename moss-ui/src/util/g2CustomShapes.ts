import G2 from '@antv/g2';

// https://www.yuque.com/antv/g2-docs/api-shape
const registerShape = G2.Shape.registerShape;
if (registerShape) {
  registerShape('polygon', 'shrinkedRectangle', {
    getPoints(pointInfo: any) {
      const points: any[] = [];
      pointInfo.x.forEach((x: number, index: number) => {
        const y = pointInfo.y[index];
        points.push({ x, y });
      });
      return points;
    },
    draw(cfg: any, container: any) {
      // @ts-ignore
      let points = this.parsePoints(cfg.points);
      const shrink = 5; // 10px padding between lines
      points = points.map((p: any, index: number) => {
        let x = p.x;
        let y = p.y;
        if (index === 0 || index === 3) {
          y -= shrink;
        } else {
          y += shrink;
        }
        if (index === 0 || index === 1) {
          x -= 1; // prevent 1 px gap
        }
        return [x, y];
      });
      const polygon = container.addShape('polygon', {
        attrs: {
          points,
          fill: cfg.color,
        },
      });
      return polygon;
    },
  });
}
