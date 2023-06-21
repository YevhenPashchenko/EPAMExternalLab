'use strict';

/**
 * You must return a date that comes in a predetermined number of seconds after 01.06.2020 00:00:002020
 * @param {number} seconds
 * @returns {Date}
 *
 * @example
 *      31536000 -> 01.06.2021
 *      0 -> 01.06.2020
 *      86400 -> 02.06.2020
 */
function secondsToDate(seconds) {
  const start = new Date(2020, 5, 1, 0, 0, 0, 2020);
  return new Date(start.valueOf() + seconds * 1000);
}

/**
 * You must create a function that returns a base 2 (binary) representation of a base 10 (decimal) string number
 * ! Numbers will always be below 1024 (not including 1024)
 * ! You are not able to use parseInt
 * @param {number} decimal
 * @return {string}
 *
 * @example
 *      5 -> "101"
 *      10 -> "1010"
 */
function toBase2Converter(decimal) {
  let number = decimal % 1024;
  let result = "";
  if (number < 1) {
    return result + number;
  }
  while (number > 1) {
    result = (number % 2) + result;
    number = Math.floor(number / 2);
  }
  if (number === 1) {
    result = number + result;
  }
  return result;
}

/**
 * You must create a function that takes two strings as arguments and returns the number of times the first string
 * is found in the text.
 * @param {string} substring
 * @param {string} text
 * @return {number}
 *
 * @example
 *      'a', 'test it' -> 0
 *      't', 'test it' -> 2
 *      'T', 'test it' -> 2
 */
function substringOccurrencesCounter(substring, text) {
  return text.toLowerCase().split(substring.toLowerCase()).length - 1;
}

/**
 * You must create a function that takes a string and returns a string in which each character is repeated once.
 *
 * @param {string} string
 * @return {string}
 *
 * @example
 *      "Hello" -> "HHeelloo"
 *      "Hello world" -> "HHeello  wworrldd" // o, l is repeated more then once. Space was also repeated
 */
function repeatingLitters(string) {
  let arr = [];
  for (const element of string) {
    if (substringOccurrencesCounter(element, string) === 1) {
      arr.push(element, element);
    } else {
      arr.push(element);
    }
  }
  return arr.join("");
}

/**
 * You must write a function redundant that takes in a string str and returns a function that returns str.
 * ! Your function should return a function, not a string.
 *
 * @param {string} str
 * @return {function}
 *
 * @example
 *      const f1 = redundant("apple")
 *      f1() ➞ "apple"
 *
 *      const f2 = redundant("pear")
 *      f2() ➞ "pear"
 *
 *      const f3 = redundant("")
 *      f3() ➞ ""
 */
function redundant(str) {
  const string = str;
  return () => {
    return string;
  };
}

/**
 * https://en.wikipedia.org/wiki/Tower_of_Hanoi
 *
 * @param {number} disks
 * @return {number}
 */
function towerHanoi(disks) {
  return Math.pow(2, disks) - 1;
}

/**
 * You must create a function that multiplies two matricies (n x n each).
 *
 * @param {array} matrix1
 * @param {array} matrix2
 * @return {array}
 *
 */
function matrixMultiplication(matrix1, matrix2) {
  let result = [];
  for (const element of matrix1) {
    let arr = [];
    for (let j = 0; j < element.length; j++) {
      let ceil = 0;
      for (let k = 0; k < element.length; k++) {
        ceil += element[k] * matrix2[k][j];
      }
      arr.push(ceil);
    }
    result.push(arr);
  }
  return result;
}

/**
 * Create a gather function that accepts a string argument and returns another function.
 * The function calls should support continued chaining until order is called.
 * order should accept a number as an argument and return another function.
 * The function calls should support continued chaining until get is called.
 * get should return all of the arguments provided to the gather functions as a string in the order specified in the order functions.
 *
 * @param {string} str
 * @return {string}
 *
 * @example
 *      gather("a")("b")("c").order(0)(1)(2).get() ➞ "abc"
 *      gather("a")("b")("c").order(2)(1)(0).get() ➞ "cba"
 *      gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get()  ➞ "hello"
 */
function gather(str) {
  let arr = [str];

  function f(string) {
    arr.push(string);
    f.order = (pos) => {
      let result = arr[pos];

      function f1(position) {
        result += arr[position];
        f1.get = () => {
          return result;
        }
        return f1;
      }

      return f1;
    }
    return f;
  }

  return f;
}

module.exports = {
  secondsToDate,
  toBase2Converter,
  substringOccurrencesCounter,
  repeatingLitters,
  redundant,
  towerHanoi,
  matrixMultiplication,
  gather
};