import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { UsuarioListComponent } from './features/usuarios/usuario-list/usuario-list.component';
import { UsuarioFormComponent } from './features/usuarios/usuario-form/usuario-form.component';
import { CategoriaListComponent } from './features/categorias/categoria-list/categoria-list.component';
import { CategoriaFormComponent } from './features/categorias/categoria-form/categoria-form.component';
import { ProductoListComponent } from './features/productos/producto-list/producto-list.component';
import { ProductoFormComponent } from './features/productos/producto-form/producto-form.component';
import { ProveedorListComponent } from './features/proveedores/proveedor-list/proveedor-list.component';
import { ProveedorFormComponent } from './features/proveedores/proveedor-form/proveedor-form.component';
import { CompraListComponent } from './features/compras/compra-list/compra-list.component';
import { CompraFormComponent } from './features/compras/compra-form/compra-form.component';
import { VentaListComponent } from './features/ventas/venta-list/venta-list.component';
import { VentaFormComponent } from './features/ventas/venta-form/venta-form.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { StockListComponent } from './features/stock/stock-list/stock-list.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SidebarComponent,
    UsuarioListComponent,
    UsuarioFormComponent,
    CategoriaListComponent,
    CategoriaFormComponent,
    ProductoListComponent,
    ProductoFormComponent,
    ProveedorListComponent,
    ProveedorFormComponent,
    CompraListComponent,
    CompraFormComponent,
    VentaListComponent,
    VentaFormComponent,
    DashboardComponent,
    StockListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    provideClientHydration(withEventReplay())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }