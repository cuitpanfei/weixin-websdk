# weixin-websdk

微信网页协议的 Go/GoFrame SDK。本项目从原 Java 版本按业务能力重新设计，Go API 不复刻 Java 的类、Singleton 或 `ServiceImpl` 层级。

> 微信网页协议并非稳定的公开 API，接口可能随微信服务端调整而失效。请仅在遵守平台条款与适用法律的场景中使用。

## 模块

- `core`：并发安全的 HTTP 会话、Cookie 隔离与类型化事件主题。
- `mp`：公众号后台扫码登录、素材查询与草稿保存。
- `wechat`：网页版微信扫码登录、初始化、联系人、消息同步以及文本/图片/视频发送。
- `examples/quickstart`：最小登录示例。

仓库使用 `go.work` 组织独立模块。每个业务客户端独占会话和认证状态，可并行运行多个账号。

## 验证

```bash
go test ./core/... ./mp/... ./wechat/...
go test -race ./core/... ./mp/... ./wechat/...
go vet ./core/... ./mp/... ./wechat/...
```

原 Java 源码暂保留在仓库中作为业务行为对照，不参与 Go 构建。
