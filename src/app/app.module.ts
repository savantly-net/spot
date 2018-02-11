import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GraphexpService, GraphexpModule } from '@savantly/ngx-graphexp';
import { GremlinClientOptions } from '@savantly/gremlin-js';

import { RouterModule, Routes } from '@angular/router';
const routes: Routes = [];

const clientOptions: GremlinClientOptions = new GremlinClientOptions();
export const graphexpService = new GraphexpService(clientOptions);

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    GraphexpModule
  ],
  providers: [{provide: GraphexpService, useValue: graphexpService}],
  bootstrap: [AppComponent]
})
export class AppModule { }
