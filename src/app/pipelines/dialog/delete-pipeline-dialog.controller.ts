export class DeletePipelineDialogController {

    $mdDialog: any;
    PipelineOperationsService: any;

    pipeline: any;
    refreshPipelines: any;
    RestApi: any;

    isInProgress: any = false;
    currentStatus: any;

    constructor($mdDialog, RestApi, pipeline, refreshPipelines) {
        this.$mdDialog = $mdDialog;
        this.RestApi = RestApi;
        this.pipeline = pipeline;
        this.refreshPipelines = refreshPipelines;
    }

    hide() {
        this.$mdDialog.hide();
    };

    cancel() {
        this.$mdDialog.cancel();
    };

    deletePipeline() {
        this.isInProgress = true;
        this.currentStatus = "Deleting pipeline...";
        this.RestApi.deleteOwnPipeline(this.pipeline._id)
            .then(data => {
                this.refreshPipelines();
                this.hide();
            });
    }

    stopAndDeletePipeline() {
        this.isInProgress = true;
        this.currentStatus = "Stopping pipeline...";
        this.RestApi.stopPipeline(this.pipeline._id)
            .then(data => {
               this.deletePipeline();
            }, data => {
                this.deletePipeline();
            });
    }


}

DeletePipelineDialogController.$inject = ['$mdDialog', 'RestApi', 'pipeline', 'refreshPipelines'];