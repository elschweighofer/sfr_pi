# Cloud Infrastructure

## set up Ubuntu Server Hosts (2)

kubeone, kubetwo
raspberry pi dependency:
sudo apt install linux-modules-extra-raspi

## Install K3s (small kubernetes)

Host 1

```
curl -sfL <https://get.k3s.io> | sh -
sudo k3s kubectl get node
sudo cat /var/lib/rancher/k3s/server/node-token #to get $NODE_TOKEN
```

takes some time, then :
NAME      STATUS   ROLES                  AGE     VERSION
kubeone   Ready    control-plane,master   4m51s   v1.26.3+k3s1
kube@kubeone:~$

Add Node to cluster:

```
§ curl -sfL https://get.k3s.io | K3S_URL= https://IP_kubeone K3S_TOKEN=$NODE_TOKEN sh - 
```

## Lens

Get Cluster Config:

```
sudo cat /etc/rancher/k3s/k3s.yaml
```

replace the localhost ip with actual ip or hostname/domain if dns is setup
save and import to lens <https://k8slens.dev>

# ArgoCD Installation in the Kubernetes Cluster

```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

# ArgoCD app Deployment

Install argo cli on dev mac:

```zsh
brew install argocd
```

Create App

```zsh
argocd app create sfrui --repo <https://github.com/elschweighofer/sfr_pi> --path frontend-deplyoment --dest-server <https://kubernetes.default.svc> --dest-namespace default
```
