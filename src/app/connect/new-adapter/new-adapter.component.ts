import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { RestService } from '../rest.service';
import { FormatDescription } from '../model/connect/grounding/FormatDescription';
import { AdapterDescription } from '../model/connect/AdapterDescription';
import { MatDialog } from '@angular/material';
import { AdapterStartedDialog } from './component/adapter-started-dialog.component';
import { Logger } from '../../shared/logger/default-log.service';
import { GenericAdapterSetDescription } from '../model/connect/GenericAdapterSetDescription';
import { GenericAdapterStreamDescription } from '../model/connect/GenericAdapterStreamDescription';
import {EventSchema} from '../schema-editor/model/EventSchema';
import {SpecificAdapterSetDescription} from '../model/connect/SpecificAdapterSetDescription';
import {SpecificAdapterStreamDescription} from '../model/connect/SpecificAdapterStreamDescription';
import {TransformationRuleDescription} from '../model/connect/rules/TransformationRuleDescription';
import {TransformationRuleService} from '../transformation-rule.service';
import {ShepherdService} from '../../services/tour/shepherd.service';

@Component({
  selector: 'sp-new-adapter',
  templateUrl: './new-adapter.component.html',
  styleUrls: ['./new-adapter.component.css'],
})
export class NewAdapterComponent implements OnInit {
  @Input()
  adapter: AdapterDescription;
  @Output()
  removeSelectionEmitter: EventEmitter<void> = new EventEmitter<void>();
  allFormats: FormatDescription[] = [];
  isLinearStepper: boolean = true;

  eventSchema: EventSchema;
  oldEventSchema: EventSchema;

  hasInputProtocol: Boolean;
  hasInputFormat: Boolean;
  hasInput: Boolean[];
  inputValue = '';

  constructor(
    private logger: Logger,
    private restService: RestService,
    private transformationRuleService: TransformationRuleService,
    public dialog: MatDialog,
    private ShepherdService: ShepherdService
  ) {}

  ngOnInit() {
    this.restService.getFormats().subscribe(x => {
      this.allFormats = x.list;
    });

    this.eventSchema = new EventSchema();
  }

  public startAdapter() {
    let dialogRef = this.dialog.open(AdapterStartedDialog, {
      // width: '250px',
      // data: { name: this.name, animal: this.animal }
    });


    this.ShepherdService.trigger("button-startAdapter");

    this.restService.addAdapter(this.adapter);

    dialogRef.afterClosed().subscribe(result => {
        this.removeSelectionEmitter.emit();
    });
  }

  removeSelection() {
      this.removeSelectionEmitter.emit();
  }

  clickSpecificSettingsNextButton() {
      this.ShepherdService.trigger("specific-settings-next-button");
  }

  clickEventSchemaNextButtonButton() {
      this.ShepherdService.trigger("event-schema-next-button");
  }

  public setSchema() {

    if (this.adapter.constructor.name == 'GenericAdapterSetDescription') {
        (<GenericAdapterSetDescription> this.adapter).dataSet.eventSchema = this.eventSchema;
    } else if (this.adapter.constructor.name == 'SpecificAdapterSetDescription'){
        (<SpecificAdapterSetDescription> this.adapter).dataSet.eventSchema = this.eventSchema;
    } else if (this.adapter.constructor.name == 'GenericAdapterStreamDescription'){
        (<GenericAdapterStreamDescription> this.adapter).dataStream.eventSchema = this.eventSchema;
    } else if (this.adapter.constructor.name == 'SpecificAdapterStreamDescription'){
        (<SpecificAdapterStreamDescription> this.adapter).dataStream.eventSchema = this.eventSchema;
    }


    this.transformationRuleService.setOldEventSchema(this.oldEventSchema);

    this.transformationRuleService.setNewEventSchema(this.eventSchema);
    const transformationRules: TransformationRuleDescription[] = this.transformationRuleService.getTransformationRuleDescriptions();
    this.adapter.rules = transformationRules;
  }

  formatSelected(selectedFormat) {
    if (
      this.adapter instanceof GenericAdapterSetDescription ||
      this.adapter instanceof GenericAdapterStreamDescription
    ) {
      this.adapter.format = selectedFormat;
    }
  }
}
