<template id="app-frame">
    <v-app>
        <v-navigation-drawer v-model="drawer" app>

        <v-list-item two-line>
                  <v-list-item-avatar>
                    <img src="https://randomuser.me/api/portraits/women/81.jpg">
                  </v-list-item-avatar>

                  <v-list-item-content>
                    <v-list-item-title>Jane Smith</v-list-item-title>
                    <v-list-item-subtitle>Logged In</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>



 <v-divider></v-divider>

 <v-list-item link href="/">

<v-list-item-icon>
            <v-icon>mdi-home</v-icon>
          </v-list-item-icon>
<v-list-item-content>
            <v-list-item-title>Inicio</v-list-item-title>
          </v-list-item-content>

 </v-list-item>

<v-list-item link href="/data-table">
<v-list-item-icon>
            <v-icon>mdi-account-multiple-outline</v-icon>
          </v-list-item-icon>
<v-list-item-content>
            <v-list-item-title>Data-Table</v-list-item-title>
          </v-list-item-content>

 </v-list-item>

        </v-navigation-drawer>

        <v-app-bar app dense>
            <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>

            <v-toolbar-title>Application</v-toolbar-title>
        </v-app-bar>

        <v-main>
            <slot></slot>
        </v-main>
    </v-app>
</template>

<script>
    Vue.component("app-frame", {
        template: "#app-frame",
        data: () => ({ drawer: null }),
    })
</script>
