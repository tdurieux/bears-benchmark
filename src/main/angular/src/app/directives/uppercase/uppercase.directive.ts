import { Directive, ElementRef, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[uppercase]'
})
export class UppercaseDirective {

  constructor(private _elementRef: ElementRef, private _control: NgControl) {
  }

  @HostListener('input', ['$event']) onEvent($event) {
    let upper = this._elementRef.nativeElement.value.toUpperCase();
    this._control.control.setValue(upper);
  }
  
}