export class DeploymentController {

    DeploymentService: any;
    deployment: any;
    resultReturned: any;
    loading: any;
    jsonld: any;
    zipFile: any;
    deploymentSettings: any;
    element: any;
    java: any;

    constructor(DeploymentService) {
        this.DeploymentService = DeploymentService;
        this.deployment = {};

        this.resultReturned = false;
        this.loading = false;
        this.jsonld = "";
        this.zipFile = "";
    }

    $onInit() {
        this.deployment.elementType = this.deploymentSettings.elementType;
    }

    generateImplementation() {
        this.resultReturned = false;
        this.loading = true;
        this.DeploymentService.generateImplementation(this.deployment, this.element)
            .success((data, status, headers, config) => {
                //$scope.openSaveAsDialog($scope.deployment.artifactId +".zip", data, "application/zip");
                this.resultReturned = true;
                this.loading = false;
                this.zipFile = data;
            }).error((data, status, headers, config) => {
            console.log(data);
            this.loading = false;
        });
    };

    generateDescription() {
        this.loading = true;
        this.DeploymentService.generateDescriptionJava(this.deployment, this.element)
            .success((data, status, headers, config) => {
                // $scope.openSaveAsDialog($scope.element.name +".jsonld", data, "application/json");
                this.loading = false;
                this.resultReturned = true;
                this.java = data;
            }).error((data, status, headers, config) => {
            this.loading = false;
        });
        this.DeploymentService.generateDescriptionJsonld(this.deployment, this.element)
            .success((data, status, headers, config) => {
                // $scope.openSaveAsDialog($scope.element.name +".jsonld", data, "application/json");
                this.loading = false;
                this.resultReturned = true;
                this.jsonld = JSON.stringify(data, null, 2);
                console.log(this.jsonld);
            }).error((data, status, headers, config) => {
            this.loading = false;
        });
    }


    openSaveAsDialog(filename, content, mediaType) {
        var blob = new Blob([content], {type: mediaType});
        // TODO: saveAs not implemented
        //this.saveAs(blob, filename);
    }
}

DeploymentController.$inject = ['DeploymentService'];