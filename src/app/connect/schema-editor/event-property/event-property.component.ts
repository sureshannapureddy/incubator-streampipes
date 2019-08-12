import { Component, EventEmitter, OnInit, Output, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventProperty } from '../model/EventProperty';
import { DomainPropertyProbabilityList } from '../model/DomainPropertyProbabilityList';
import { DomainPropertyProbability } from '../model/DomainPropertyProbability';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EventPropertyPrimitive } from '../model/EventPropertyPrimitive';

@Component({
  selector: 'app-event-property',
  templateUrl: './event-property.component.html',
  styleUrls: ['./event-property.component.css']
})
export class EventPropertyComponent implements OnInit {

  private propertyForm: FormGroup;
  // protected dataTypes = dataTypes;

  @Output() propertyChange = new EventEmitter<EventProperty>();
  domainPropertyGuess: any;

  @Output() save: EventEmitter<EventProperty> = new EventEmitter<EventProperty>();

  constructor(@Inject(MAT_DIALOG_DATA) public property: EventProperty, private formBuilder: FormBuilder) {
  }


  private createForm() {
    this.propertyForm = this.formBuilder.group({
      label: [this.property.getLabel(), Validators.required],
      runtimeName: [this.property.getRuntimeName(), Validators.required],
      description: [this.property.getDescription(), Validators.required],
      domainProperty: ['', Validators.required],
      dataType: ['', Validators.required]
    });
  }

  private isEventPropertyPrimitive(instance): boolean {
    return instance instanceof EventPropertyPrimitive;
  }

  staticValueAddedByUser() {
    if (this.property.id.startsWith('http://eventProperty.de/staticValue/')) {
      return true;
    } else {
      return false;
    }

  }

  ngOnInit(): void {
    this.createForm();

    if (this.domainPropertyGuess == null) {
      this.domainPropertyGuess = new DomainPropertyProbabilityList();
    }

    const tmpBestDomainProperty = this.getDomainPropertyWithHighestConfidence(this.domainPropertyGuess.list);

    if (tmpBestDomainProperty != null) {
      this.property.domainProperty = tmpBestDomainProperty.domainProperty;
    }


  }

  private getDomainPropertyWithHighestConfidence(list: DomainPropertyProbability[]): DomainPropertyProbability {
    var result: DomainPropertyProbability = null;

    for (var _i = 0; _i < list.length; _i++) {
      if (result == null || +result.probability < +list[_i].probability) {
        result = list[_i];
      }
    }

    return result;
  }

  // aufgerufen, wenn FormGroup valide ist
  submit(): void {
    // const writeJsonService: WriteJsonService = WriteJsonService.getInstance();
    // const dragDropService: DragDropService = DragDropService.getInstance();

    this.property.label = this.propertyForm.value.label;
    this.property.description = this.propertyForm.value.description;
    this.property.runTimeName = this.propertyForm.value.runtimeName;
    this.property.domainProperty = encodeURIComponent(this.propertyForm.value.domainProperty);

    // const path: string = dragDropService.buildPath(this.property);
    // writeJsonService.add(path, this.propertyForm.value.runtimeName);
    // this.save.emit(this.property);
  }
}
