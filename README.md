# Autoscaling Spring Boot with the Horizontal Pod Autoscaler and custom metrics on Kubernetes

## Installing Custom Metrics Api

Make sure you are in the `monitoring` folder:

```bash
cd monitoring
```

Deploy the Metrics Server in the `kube-system` namespace:

```bash
kubectl create -f ./metrics-server
```

After one minute the metric-server starts reporting CPU and memory usage for nodes and pods.

View nodes metrics:

```bash
kubectl get --raw "/apis/metrics.k8s.io/v1beta1/nodes" | jq .
```

View pods metrics:

```bash
kubectl get --raw "/apis/metrics.k8s.io/v1beta1/pods" | jq .
```

Create the monitoring namespace:

```bash
kubectl create -f ./namespaces.yaml
```

Deploy Prometheus v2 in the monitoring namespace:

```bash
kubectl create -f ./prometheus
```

Generate the TLS certificates needed by the Prometheus adapter:

```bash
make certs
```

Deploy the Prometheus custom metrics API adapter:

```bash
kubectl create -f ./custom-metrics-api
```

List the custom metrics provided by Prometheus:

```bash
kubectl get --raw "/apis/custom.metrics.k8s.io/v1beta1" | jq .
```

Get the FS usage for all the pods in the `monitoring` namespace:

```bash
kubectl get --raw "/apis/custom.metrics.k8s.io/v1beta1/namespaces/monitoring/pods/*/fs_usage_bytes" | jq .
```