/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.controller;

import com.dvd.ckp.business.service.CustomerService;
import com.dvd.ckp.domain.Customers;
import com.dvd.ckp.utils.SpringConstant;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

/**
 *
 * @author dmin
 */
public class CustomerController extends GenericForwardComposer {

    private static final Logger logger = Logger.getLogger(CustomerController.class);
    @WireVariable
    protected CustomerService customerService;
    @Wire
    private Grid lstCustomer;
    @Wire
    private Textbox txtFilterCode;
    ListModelList<Customers> listDataModel;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerService = (CustomerService) SpringUtil.getBean(SpringConstant.CUSTOMER_SERVICES);
        List<Customers> vlstCustomer = customerService.getAllCustomer();
        listDataModel = new ListModelList(vlstCustomer);
        lstCustomer.setModel(listDataModel);
    }

    /**
     * Edit row
     *
     * @param event
     */
    public void onEdit(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        setEnableComponent(lstCell);
    }

    /**
     * Set style enable edit
     *
     * @param lstCell
     */
    private void setEnableComponent(List<Component> lstCell) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getChildren().get(0);
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(true);
                        ((Combobox) child).setInplace(false);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(false);
                        ((Textbox) child).setInplace(false);
                    } else if (child instanceof A) {
                        A edit = (A) child;
                        edit.setVisible(false);
                        A save = (A) c.getChildren().get(1);
                        A cancel = (A) c.getChildren().get(2);
                        save.setVisible(true);
                        cancel.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * Set style disable edit
     *
     * @param lstCell
     */
    private void setDisableComponent(List<Component> lstCell) {
        if (lstCell != null && !lstCell.isEmpty()) {
            for (Component c : lstCell) {
                if (c instanceof Cell) {
                    Component child = c.getChildren().get(0);
                    if (child instanceof Combobox) {
                        ((Combobox) child).setButtonVisible(false);
                        ((Combobox) child).setInplace(true);
                    } else if (child instanceof Textbox) {
                        ((Textbox) child).setReadonly(true);
                        ((Textbox) child).setInplace(true);
                    } else if (child instanceof A) {
                        A edit = (A) child;
                        edit.setVisible(true);
                        A save = (A) c.getChildren().get(1);
                        A cancel = (A) c.getChildren().get(2);
                        save.setVisible(false);
                        cancel.setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * Cancel
     *
     * @param event
     */
    public void onCancel(ForwardEvent event) {

        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        setDisableComponent(lstCell);
        reloadGrid();

    }

    /**
     * Save
     *
     * @param event
     */
    public void onSave(ForwardEvent event) {
        Row rowSelected = (Row) event.getOrigin().getTarget().getParent().getParent();
        List<Component> lstCell = rowSelected.getChildren();
        Customers c = rowSelected.getValue();
        Customers customer = getDataInRow(lstCell);
        customer.setCustomerId(c.getCustomerId());
        customerService.insertOrUpdateCustomer(customer);
        setDisableComponent(lstCell);
        reloadGrid();
    }

    /**
     * Add row
     */
    public void onClick$add() {
        Customers customer = new Customers();
        listDataModel.add(customer);
        lstCustomer.renderAll();
        List<Component> lstCell = lstCustomer.getRows().getChildren().get(listDataModel.size() - 1).getChildren();
        setEnableComponent(lstCell);
    }

    /**
     * Get object customer
     *
     * @param lstCell
     * @return
     */
    private Customers getDataInRow(List<Component> lstCell) {
        Customers customer = new Customers();
        Textbox txtCustomerCode = (Textbox) lstCell.get(1).getFirstChild();
        Textbox txtCustomerName = (Textbox) lstCell.get(2).getFirstChild();
        Textbox txtCustomerPhone = (Textbox) lstCell.get(3).getFirstChild();
        Textbox txtTaxCode = (Textbox) lstCell.get(4).getFirstChild();
        Textbox txtCustomerAddress = (Textbox) lstCell.get(5).getFirstChild();
        Textbox txtAccountNumber = (Textbox) lstCell.get(6).getFirstChild();
        Textbox txtBankName = (Textbox) lstCell.get(7).getFirstChild();
        Combobox cbxStatus = (Combobox) lstCell.get(8).getFirstChild();
        customer.setCustomerCode(txtCustomerCode.getValue());
        customer.setCustomerName(txtCustomerName.getValue());
        customer.setCustomerPhone(txtCustomerPhone.getValue());
        customer.setCustomerAddress(txtCustomerAddress.getValue());
        customer.setTaxCode(txtTaxCode.getValue());
        customer.setAccountNumber(txtAccountNumber.getValue());
        customer.setBankName(txtBankName.getValue());
        customer.setStatus(Integer.valueOf(cbxStatus.getSelectedItem().getValue()));
        return customer;
    }

    /**
     * Reload grid
     */
    private void reloadGrid() {
        List<Customers> vlstCustomer = customerService.getAllCustomer();
        listDataModel = new ListModelList(vlstCustomer);
        lstCustomer.setModel(listDataModel);
    }

    public void onChange$txtFilterCode() {
        Customers customer = new Customers();
        String vstrCustomerCode = txtFilterCode.getValue();
        customer.setCustomerCode(vstrCustomerCode);
        filter(customer);
    }

    public void onChange$txtFilterName() {

    }

    private void filter(Customers customer) {
        List<Customers> vlstCustomer = customerService.getCustomer(customer);
        listDataModel = new ListModelList(vlstCustomer);
        lstCustomer.setModel(listDataModel);
        
    }
}
