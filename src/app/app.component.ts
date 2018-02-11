import { LabelService } from './graph/label.service';
import { Component } from '@angular/core';
import { GraphConfig, GraphexpService } from '@savantly/ngx-graphexp';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  graphConfig: GraphConfig = new GraphConfig();

  constructor (public service: GraphexpService, private labels: LabelService) {
    this.labels.labels.subscribe(response => {
      this.graphConfig.nodeLabels = response.node;
      this.graphConfig.linkLabels = response.link;
    });
  }

}
