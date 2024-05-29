### TODO:
* Removing the "hard-code" in Better Chat feature to support wide variety of servers.
* Removing `try...catch` statement in MixinMouseHandler class, which prevents crashing the game from ConcurrentModificationException.
* Improving GUI "hitboxes" for Better Chat tabs (eg. Making them larger).
* Toggle in settings to make the Better Chat tabs show above chat.
* Improving FPS data capture for periodic telemetry. We should ignore 0 FPS...
### Issues:
* Closing Better Chat private msg tabs while they are active doesn't make it fall back perfectly to default ("All") tab.
* When private msg tabs from Better Chat have new messages the color of tab name is not changed.
* Fixing hover action in mod configuration.