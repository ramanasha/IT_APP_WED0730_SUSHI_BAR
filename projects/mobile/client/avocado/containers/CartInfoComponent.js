import React, { Component } from 'react';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { View, StyleSheet} from 'react-native';
import {Text, Button} from 'react-native-elements';
import constants from '../constants/constants'
import translate from "translatr";
import dictionary from '../translations/translations';
import {addOrder, emptyCart, postOrder} from '../actions/index';
import uuidv3 from 'uuid';
import { NavigationActions } from "react-navigation";



class CartInfoComponent extends Component {

    render(){
        return (
            <View style={style.mainViewStyle}>
                <View style={{flex: 3}}>
                    <Text style={style.infoTextStyle}>{translate(dictionary, 'sum', this.props.lang || 'en').sum}: {this.props.sum} zl</Text>
                    <Text style={style.infoTextStyle}>{translate(dictionary, 'estimatedTime', this.props.lang || 'en').estimatedTime}: {this.props.estimatedTime} min</Text>
                </View>
                <View style={{flex: 2}}>
                    <Button
                        title = 'ORDER'
                        color = {constants.colors.white}
                        onPress = { () => {
                            let itemsInCartToPost = this.props.itemsInCart.map( (item) => {
                                return {
                                    meal: {
                                        id: item.mealId,
                                    },
                                    amount: item.amount
                                }
                            });
                            let orderId = uuidv3();
                            this.props.addOrder(orderId, this.props.itemsInCart,'ORDERED', this.props.sum, this.props.estimatedTime).then( () => {
                                let price = this.props.sum;
                                this.props.emptyCart();
                                this.props.postOrder(orderId, this.props.table, itemsInCartToPost, price ).then( () => {
                                    this.props.navi.dispatch(
                                        NavigationActions.navigate({ routeName: "Menu" })
                                    );
                                })

                            })
                        }}
                        buttonStyle={{
                            backgroundColor: constants.colors.green,
                        }}
                        disabled = {this.props.sum === 0}
                    />
                </View>
            </View>
        )
    }
}

const mapStateToProps = (state) => ({
    language: state.i18nReducer.currentLanguage,
    sum: state.CartReducer.sum,
    estimatedTime: state.CartReducer.estimatedTime,
    itemsInCart: state.CartReducer.itemsInCart,
    table: state.OrderReducer.table
});

const mapDispatchToProps = (dispatch) => {
    return bindActionCreators({
        addOrder: addOrder,
        emptyCart: emptyCart,
        postOrder: postOrder
    }, dispatch)
};

const style = StyleSheet.create({
    infoTextStyle: {
        fontSize: 16,
        marginBottom: 5
    },
    mainViewStyle: {
        marginLeft: 15,
        marginTop: 15,
        flexDirection: 'row',
        alignItems: 'stretch'
    }
});


export default connect(mapStateToProps, mapDispatchToProps)(CartInfoComponent);