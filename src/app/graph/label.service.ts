import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class LabelService {

  get labels(): Observable<any> {
    return this.http.get('./rest/modules/spot/labels');
  }

  constructor(private http: HttpClient) { }

}
